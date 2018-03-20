package com.zou.core.initializer;

import com.zou.netty.initializer.RpcReceiveSerializableSelection;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class MessageReceiveChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializableProtocol protocol;

    private RpcReceiveSerializableSelection selection = null;


    public MessageReceiveChannelInitializer buildRpcSerializableProtocol(RpcSerializableProtocol protocol) {
        this.protocol = protocol;
        return this;
    }


    public MessageReceiveChannelInitializer(Map<String, Object> map) {
        selection = new RpcReceiveSerializableSelection(map);
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        selection.select(protocol, pipeline);

    }
}




































