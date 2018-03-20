package com.zou.netty.task;

import com.google.common.util.concurrent.*;
import com.zou.config.SystemConfig;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.parallel.pool.RpcThreadPool;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RpcServerLoader {

    private static volatile RpcServerLoader rpcServerLoader;

    private static final String DELIMITER = SystemConfig.DELIMITER;

    private RpcSerializableProtocol rpcSerializableProtocol = RpcSerializableProtocol.JDK_SERIALIZABLE;

    private static final int PARALLEL = SystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);

    private static int threadNums = SystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;

    private static int queueNums = SystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcThreadPool.getExecutor(threadNums, queueNums));

    private MessageSendHandler messageSendHandler = null;
    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handleStatus = lock.newCondition();


    private RpcServerLoader() {

    }


    public static RpcServerLoader getInstance() {

        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {

                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }

        return rpcServerLoader;
    }


    public void load(String serverAddress, RpcSerializableProtocol serializableProtocol) {
        String[] ipAddress = serverAddress.split(RpcServerLoader.DELIMITER);

        if (ipAddress.length == SystemConfig.IP_ADDRESS_OPRT_ARRAY_LENGTH) {
            String host = ipAddress[0];
            int port = Integer.parseInt(ipAddress[1]);

            final InetSocketAddress remoteAddress = new InetSocketAddress(host, port);

            System.out.printf("zrpc  Client  start success!\nip:%s\nport:%d\nprotocol:%s\n\n", host, port, serializableProtocol);

            ListenableFuture<Boolean> listenableFuture = listeningExecutorService.submit(new MessageSendInitializableTask(eventLoopGroup, remoteAddress, serializableProtocol));

            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {


                    try {

                        lock.lock();
                        if (messageSendHandler == null) {
                            handleStatus.await();

                            if (result.equals(Boolean.TRUE) && messageSendHandler != null) {
                                connectStatus.signalAll();
                            }
                        }
                    } catch (InterruptedException e) {

                        Logger.getLogger(RpcServerLoader.class.getName()).log(Level.SEVERE, null, e);

                    } finally {
                        lock.unlock();
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, listeningExecutorService);

        }

    }


    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
        try {

            lock.lock();
            if (messageSendHandler == null) {
                connectStatus.await();
            }
            return messageSendHandler;

        } finally {
            lock.unlock();
        }
    }

    public void setMessageSendHandler(MessageSendHandler messageSendHandler) {
        try {
            lock.lock();
            this.messageSendHandler = messageSendHandler;
            handleStatus.signal();

        } finally {
            lock.unlock();

        }
    }


    public void unLoad() {

        messageSendHandler.close();
        listeningExecutorService.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void setRpcSerializableProtocol(RpcSerializableProtocol rpcSerializableProtocol) {
        this.rpcSerializableProtocol = rpcSerializableProtocol;
    }
}





































































