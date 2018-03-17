package com.zou.serializable.message;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @author zlz
 * <p>
 * 帮助类，定义基本的编码解码的方法
 */

public interface MessageCodeHelper {

    /**
     * 定义消息长度
     */
    int MESSAGE_LENGTH = 4;

    /**
     * @param out
     * @param message
     * @throws IOException
     * 编码器
     */
    void encode(final ByteBuf out, final Object message) throws IOException;

    /**
     * @param bytes
     * @return
     * @throws Exception
     * 解码器
     */
    Object decode(byte[] bytes) throws Exception;


}






































