package com.zou.serializable.message;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 消息解码器
 * <p>
 * 继承Netty提供处理器，将字节转换为消息
 * <p>
 * ByteToMessageDecoder 提供的处理器
 */
public class MessageDecoder extends ByteToMessageDecoder {

    /**
     *  消息长度
     */

    public static final int MESSAGE_LENGTH = MessageCodeHelper.MESSAGE_LENGTH;

    private MessageCodeHelper helper = null;

    public MessageDecoder(MessageCodeHelper helper) {
        this.helper = helper;
    }

    /**
     * @param ctx
     * @param in
     * @param out
     * @throws Exception 实现decode方法
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < MessageCodeHelper.MESSAGE_LENGTH) {
            return;
        }

        //标记当前Buffer读索引的位置
        in.markReaderIndex();

        int messageLength = in.readInt();

        if (messageLength < 0) {
            ctx.close();
        }

        //Buffer中可读的字节数  小于消息长度
        if (in.readableBytes() < messageLength) {
            in.resetReaderIndex();
            return;
        } else {

            byte[] message = new byte[messageLength];
            in.readBytes(message);

            try {
                Object object = helper.decode(message);
                out.add(object);
            } catch (IOException e) {
                Logger.getLogger(MessageDecoder.class.getName()).log(Level.SEVERE, null, e);
            }
        }


    }
}









































































