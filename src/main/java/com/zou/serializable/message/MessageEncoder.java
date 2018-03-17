package com.zou.serializable.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zlz
 * 编码器
 * 继承Netty提供的编码器，将消息转换为字节
 * MessageToByteEncoder
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {


    private MessageCodeHelper helper = null;

    public MessageEncoder(MessageCodeHelper helper) {
        this.helper = helper;
    }


    //实现编码的方法
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        helper.encode(out, msg);

    }
}






























