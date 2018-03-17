package com.zou.netty.handlerImpl.hessian;

import com.zou.netty.handlerInterface.NettyRpcReceiveHandler;
import com.zou.netty.messageHandler.MessageReceiveHandler;
import com.zou.serializable.hessian.HessianCodecHelper;
import com.zou.serializable.hessian.HessianDecoder;
import com.zou.serializable.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class HessianReceiveHandler implements NettyRpcReceiveHandler {

    @Override
    public void handle(Map<String, Object> map, ChannelPipeline pipeline) {

        HessianCodecHelper helper = new HessianCodecHelper();
        pipeline.addLast(new HessianEncoder(helper));
        pipeline.addLast(new HessianDecoder(helper));
        pipeline.addLast(new MessageReceiveHandler(map));
    }
}





































































