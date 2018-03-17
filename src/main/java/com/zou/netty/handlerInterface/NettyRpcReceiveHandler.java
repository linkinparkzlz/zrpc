package com.zou.netty.handlerInterface;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

public interface NettyRpcReceiveHandler {

    void handle(Map<String, Object> map, ChannelPipeline pipeline);

}
