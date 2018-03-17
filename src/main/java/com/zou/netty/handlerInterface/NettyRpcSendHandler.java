package com.zou.netty.handlerInterface;

import io.netty.channel.ChannelPipeline;

public interface NettyRpcSendHandler {

    void handle(ChannelPipeline pipeline);
}
