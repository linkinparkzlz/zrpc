package com.zou.netty.executor;

import com.google.common.util.concurrent.*;
import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.compiler.AccessAdaptiveProvider;
import com.zou.config.SystemConfig;
import com.zou.core.AbilityDetailProvider;
import com.zou.core.initializer.MessageReceiveChannelInitializer;
import com.zou.jmx.ModuleMetricsHandler;
import com.zou.keyvalue.MessageKeyAndValue;
import com.zou.netty.resolver.ApiEchoResolver;
import com.zou.netty.thread.NamedThreadFactory;
import com.zou.parallel.pool.RpcThreadPool;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 当一个类实现了ApplicationContextAware这个接口，
 * 那么这个类就可以获取在Spring配置文件中所配置的Bean。
 */
public class MessageReceiveExecutor implements ApplicationContextAware {

    //地址
    private String serverIpAddress;
    //端口
    private int port;
    //所采用的序列化协议，此处使用开源的工具以及JDK原生的序列化机制帮助我们进行序列化
    private RpcSerializableProtocol serializableProtocol = RpcSerializableProtocol.JDK_SERIALIZABLE;
    //分隔符
    private static final String DELIMITER = SystemConfig.DELIMITER;
    private static final int PARALLEL = SystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;
    private static final int threadNumbers = SystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;
    //线程数
    private static final int queueNumbers = SystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    //异步调用的线程类  guvava提供的
    private static volatile ListeningExecutorService listeningExecutorService;
    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();
    private int numberOfThreadsPool = 1;

    ThreadFactory threadFactory = new NamedThreadFactory("zrpc ThreadFactory");

    //Netty提供的线程循环组。对于服务器来讲，使用worker和boss的模式
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup(PARALLEL, threadFactory, SelectorProvider.provider());


    //构造方法私有化，此处使用单例模式
    private MessageReceiveExecutor() {

        handlerMap.clear();
        register();

    }


    private void register() {

        handlerMap.put(SystemConfig.RPC_COMPLIER_SPI_ATTRIBUATE, new AccessAdaptiveProvider());
        handlerMap.put(SystemConfig.RPC_ABILITY_DETAIL_SPI_ATTRIBUTE, new AbilityDetailProvider());
    }



    public static MessageReceiveExecutor getInstance() {
        return MessageReceiveExecutorHolder.INSTANCE;
    }


    //获取实例，是单例的
    private static class MessageReceiveExecutorHolder {

        static final MessageReceiveExecutor INSTANCE = new MessageReceiveExecutor();

    }


    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }



    public static void submit(Callable<Boolean> task, final ChannelHandlerContext context, MessageRequest request, MessageResponse response) {

        //这里使用了双检查锁机制
        if (listeningExecutorService == null) {

            synchronized (MessageReceiveExecutor.class) {

                if (listeningExecutorService == null) {

                    listeningExecutorService = MoreExecutors.listeningDecorator((ThreadPoolExecutor) (SystemConfig.isMonitorServerSupport() ?
                            RpcThreadPool.getExecutorWithJmx(threadNumbers, queueNumbers) : RpcThreadPool.getExecutor(threadNumbers, queueNumbers)));
                }
            }
        }


        /**
         * Google Guaua提供的类，继承自jdk并发包中的Future，用于执行异步调用
         */
        ListenableFuture<Boolean> listenableFuture = listeningExecutorService.submit(task);

        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                context.writeAndFlush(response).addListener((ChannelFutureListener) future ->
                        System.out.println("server send message-id response : " + request.getMessageId()));

            }

            @Override
            public void onFailure(Throwable t) {

                t.printStackTrace();
            }
        }, listeningExecutorService);


    }


    /**
     * 通过这个方法就可以获得在配置文件中配置的Bean。
     *
     * @param applicationContext
     * @throws BeansException
     */

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


        try {

            //使用反射的方式获取Bean
            MessageKeyAndValue keyAndValue = (MessageKeyAndValue) applicationContext.getBean(Class.forName("com.zou.keyvalue.MessageKeyAndValue"));
            Map<String, Object> map = keyAndValue.getMessageKeyAndValue();

            Set set = map.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = set.iterator();

            Map.Entry<String, Object> entry;

            while (iterator.hasNext()) {
                entry = iterator.next();
                handlerMap.put(entry.getKey(), entry.getValue());
            }


        } catch (ClassNotFoundException e) {
            Logger.getLogger(MessageReceiveExecutor.class.getName()).log(Level.SEVERE, null, e);

        }

    }


    public void start() {


        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new MessageReceiveChannelInitializer(handlerMap).buildRpcSerializableProtocol(serializableProtocol))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] ipAddress = serverIpAddress.split(MessageReceiveExecutor.DELIMITER);


            if (ipAddress.length == SystemConfig.IP_ADDRESS_OPRT_ARRAY_LENGTH) {

                final String host = ipAddress[0];
                final int port = Integer.parseInt(ipAddress[1]);


                ChannelFuture channelFuture = null;

                channelFuture = bootstrap.bind(host, port).sync();

                channelFuture.addListener((ChannelFutureListener) future -> {


                    if (future.isSuccess()) {

                        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreadsPool);

                        ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executorService);

                        completionService.submit(new ApiEchoResolver(host, port));

                        System.out.printf("ZRpc Server start success!\nip地址:%s\n端口:%d\n协议:%s\nstart-time:%s\njmx-invoke-metrics:%s\n\n",
                                host, port, serializableProtocol, ModuleMetricsHandler.getStartTime(), (SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT ? "open" : "close"));
                    }

                });


            } else {
                System.out.printf("zrpc server start fail\n");


            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }


    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public RpcSerializableProtocol getSerializableProtocol() {
        return serializableProtocol;
    }

    public void setSerializableProtocol(RpcSerializableProtocol serializableProtocol) {
        this.serializableProtocol = serializableProtocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}














































