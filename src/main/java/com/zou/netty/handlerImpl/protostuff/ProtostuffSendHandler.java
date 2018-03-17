package com.zou.netty.handlerImpl.protostuff;

import com.zou.netty.handlerInterface.NettyRpcSendHandler;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.serializable.protostuff.ProtostuffCodecHelper;
import com.zou.serializable.protostuff.ProtostuffDecoder;
import com.zou.serializable.protostuff.ProtostuffEncoder;
import io.netty.channel.ChannelPipeline;

public class ProtostuffSendHandler implements NettyRpcSendHandler {

    @Override
    public void handle(ChannelPipeline pipeline) {
        ProtostuffCodecHelper helper = new ProtostuffCodecHelper();
        helper.setRpcDirect(false);
        pipeline.addLast(new ProtostuffEncoder(helper));
        pipeline.addLast(new ProtostuffDecoder(helper));
        pipeline.addLast(new MessageSendHandler());

    }
}
