package com.zou.netty.handlerImpl.protostuff;

import com.zou.netty.handlerInterface.NettyRpcReceiveHandler;
import com.zou.netty.handlerInterface.NettyRpcSendHandler;
import com.zou.netty.messageHandler.MessageReceiveHandler;
import com.zou.serializable.protostuff.ProtostuffCodecHelper;
import com.zou.serializable.protostuff.ProtostuffDecoder;
import com.zou.serializable.protostuff.ProtostuffEncoder;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class ProtostuffReceiveHandler implements NettyRpcReceiveHandler {

    @Override
    public void handle(Map<String, Object> map, ChannelPipeline pipeline) {

        ProtostuffCodecHelper helper = new ProtostuffCodecHelper();
        helper.setRpcDirect(true);
        pipeline.addLast(new ProtostuffEncoder(helper));
        pipeline.addLast(new ProtostuffDecoder(helper));
        pipeline.addLast(new MessageReceiveHandler(map));


    }
}




































