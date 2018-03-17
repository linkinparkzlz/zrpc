package com.zou.netty.handlerImpl.kryo;

import com.zou.netty.handlerInterface.NettyRpcReceiveHandler;
import com.zou.netty.messageHandler.MessageReceiveHandler;
import com.zou.serializable.kryo.KryoCodecHelper;
import com.zou.serializable.kryo.KryoDecoder;
import com.zou.serializable.kryo.KryoEncoder;
import com.zou.serializable.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class KryoReceiveHandler implements NettyRpcReceiveHandler {

    @Override
    public void handle(Map<String, Object> map, ChannelPipeline pipeline) {

        KryoCodecHelper helper = new KryoCodecHelper(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(helper));
        pipeline.addLast(new KryoDecoder(helper));
        pipeline.addLast(new MessageReceiveHandler(map));


    }
}


















