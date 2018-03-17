package com.zou.serializable.rpc;

import io.netty.channel.ChannelPipeline;

/**
 * @author zoulvzhou
 * <p>
 * 选择序列化协议的类型
 */

public interface RpcSerializableType {

    void selectProtocol(RpcSerializableProtocol protocol, ChannelPipeline pipeline);
}
