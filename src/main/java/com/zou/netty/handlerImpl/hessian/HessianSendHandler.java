package com.zou.netty.handlerImpl.hessian;

import com.zou.netty.handlerInterface.NettyRpcSendHandler;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.serializable.hessian.HessianCodecHelper;
import com.zou.serializable.hessian.HessianDecoder;
import com.zou.serializable.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;

public class HessianSendHandler implements NettyRpcSendHandler {

    @Override
    public void handle(ChannelPipeline pipeline) {

        HessianCodecHelper helper = new HessianCodecHelper();
        pipeline.addLast(new HessianEncoder(helper));
        pipeline.addLast(new HessianDecoder(helper));
        pipeline.addLast(new MessageSendHandler());
    }
}
