package com.zou.core.initializer;

import com.zou.netty.initializer.RpcSendSerializableSelection;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializableProtocol protocol;
    private RpcSendSerializableSelection frame = new RpcSendSerializableSelection();

    public MessageSendChannelInitializer buildRpcSerializableProtocol(RpcSerializableProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        frame.select(protocol, pipeline);

    }
}







































































