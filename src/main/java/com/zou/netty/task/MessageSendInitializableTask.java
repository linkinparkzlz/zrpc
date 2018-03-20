package com.zou.netty.task;

import com.zou.core.initializer.MessageSendChannelInitializer;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

public class MessageSendInitializableTask implements Callable<Boolean> {

    private EventLoopGroup eventLoopGroup = null;

    private InetSocketAddress inetSocketAddress = null;

    private RpcSerializableProtocol protocol;

    public MessageSendInitializableTask(EventLoopGroup eventLoopGroup, InetSocketAddress inetSocketAddress, RpcSerializableProtocol protocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.inetSocketAddress = inetSocketAddress;
        this.protocol = protocol;
    }

    @Override
    public Boolean call() throws Exception {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
                .remoteAddress(inetSocketAddress);
        bootstrap.handler(new MessageSendChannelInitializer().buildRpcSerializableProtocol(protocol));


        ChannelFuture channelFuture = bootstrap.connect();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (channelFuture.isSuccess()) {

                    MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);

                }


            }
        });

        return Boolean.TRUE;

    }
}
















































































