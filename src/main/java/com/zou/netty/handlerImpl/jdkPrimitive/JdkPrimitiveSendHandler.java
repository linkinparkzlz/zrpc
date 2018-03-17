package com.zou.netty.handlerImpl.jdkPrimitive;

import com.zou.netty.handlerInterface.NettyRpcSendHandler;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.serializable.message.MessageCodeHelper;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class JdkPrimitiveSendHandler implements NettyRpcSendHandler {

    @Override
    public void handle(ChannelPipeline pipeline) {

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
                MessageCodeHelper.MESSAGE_LENGTH, 0, MessageCodeHelper.MESSAGE_LENGTH));

        pipeline.addLast(new LengthFieldPrepender(MessageCodeHelper.MESSAGE_LENGTH));
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        pipeline.addLast(new MessageSendHandler());

    }
}


























































