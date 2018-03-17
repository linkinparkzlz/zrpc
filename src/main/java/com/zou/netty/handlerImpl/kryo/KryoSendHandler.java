package com.zou.netty.handlerImpl.kryo;

import com.zou.netty.handlerInterface.NettyRpcSendHandler;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.serializable.kryo.KryoCodecHelper;
import com.zou.serializable.kryo.KryoDecoder;
import com.zou.serializable.kryo.KryoEncoder;
import com.zou.serializable.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;

public class KryoSendHandler implements NettyRpcSendHandler {

    @Override
    public void handle(ChannelPipeline pipeline) {

        KryoCodecHelper helper = new KryoCodecHelper(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(helper));
        pipeline.addLast(new KryoDecoder(helper));
        pipeline.addLast(new MessageSendHandler());


    }
}
