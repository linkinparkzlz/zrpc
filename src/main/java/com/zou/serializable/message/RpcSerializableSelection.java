package com.zou.serializable.message;

import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.channel.ChannelPipeline;

/**
 * @author zoulvzhou
 * <p>
 * 选择序列化方式的接口
 */

public interface RpcSerializableSelection {

    /**
     * 选择使用哪种序列化方式
     *
     * @param protocol
     * @param pipeline
     */
    void select(RpcSerializableProtocol protocol, ChannelPipeline pipeline);
}
