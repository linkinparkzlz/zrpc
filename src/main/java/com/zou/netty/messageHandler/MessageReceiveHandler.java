package com.zou.netty.messageHandler;

import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.netty.executor.MessageReceiveExecutor;
import com.zou.netty.initializer.ReceiveInitializeComponent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.Callable;

public class MessageReceiveHandler extends ChannelInboundHandlerAdapter {
    private final Map<String, Object> map;

    public MessageReceiveHandler(Map<String, Object> map) {
        this.map = map;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();

        ReceiveInitializeComponent facade = new ReceiveInitializeComponent(request, response, map);

        Callable<Boolean> receveiveTask = facade.getTask();

        MessageReceiveExecutor.submit(receveiveTask, ctx, request, response);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();

    }
}
