package com.zou.netty.resolver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.util.concurrent.Callable;

public class ApiEchoResolver implements Callable<Boolean> {


    private static final boolean SSL = System.getProperty("ssl") != null;

    private String host;

    private int port;

    public ApiEchoResolver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Boolean call() throws Exception {


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            SslContext sslContext = null;

            if (SSL) {

                SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();

                sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();

            }

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ApiEchoInitializer(sslContext));

            Channel channel = bootstrap.bind(port).sync().channel();

            System.err.println("You can open your web browser see zrpc server api interface: " +
                    (SSL ? "https" : "http") + "://" + host + ":" + port + "/zrpc.html");

            channel.closeFuture().sync();

            return Boolean.TRUE;

        } catch (Exception e) {

            e.printStackTrace();
            return Boolean.FALSE;
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}












































