package com.zou.netty.messageHandler;

import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.core.MessageCallBack;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class MessageSendHandler extends ChannelInboundHandlerAdapter {


    private ConcurrentHashMap<String, MessageCallBack> map = new ConcurrentHashMap<>();
    private volatile Channel channel;
    private SocketAddress address;


    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getAddress() {
        return address;
    }


    /**
     * pipline激活后的方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.address = this.channel.remoteAddress();
    }

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }


    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageResponse response = (MessageResponse) msg;
        String messageId = response.getMessageId();

        MessageCallBack messageCallBack = map.get(messageId);

        if (messageCallBack != null) {
            map.remove(messageId);
            messageCallBack.over(response);
        }

    }

    /**
     * 发生异常后进行的处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 关闭时进行的处理
     */

    public void close() {

        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * 发送消息请求
     *
     * @param request
     * @return
     */
    public MessageCallBack sendRequest(MessageRequest request) {

        MessageCallBack messageCallBack = new MessageCallBack(request);
        map.put(request.getMessageId(), messageCallBack);
        channel.writeAndFlush(request);
        return messageCallBack;
    }

}







































































