package com.zou.netty.resolver;


import com.zou.config.SystemConfig;
import com.zou.core.AbilityDetailProvider;
import com.zou.jmx.ModuleMetricsProcessor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

public class ApiEchoHandler extends ChannelInboundHandlerAdapter {

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String CONNECTION = "Connection";

    private static final String KEEP_ALIVE = "Keep-alive";

    private static final String METRICS = "metrics";

    private static final String METRICS_ERR_MSG = "zrpc.jmx.invoke.metrics.attribute is closed";

    public ApiEchoHandler() {

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;

            byte[] content = buildResponseMessage(httpRequest);

            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));

            fullHttpResponse.headers().set(CONTENT_TYPE, "text/html");

            fullHttpResponse.headers().set(CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
            fullHttpResponse.headers().set(CONTENT_LENGTH, KEEP_ALIVE);
            ctx.write(fullHttpResponse);

        }


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private byte[] buildResponseMessage(HttpRequest request) {

        byte[] content = null;

        boolean metrics = (request.getUri().indexOf(METRICS) != -1);

        if (SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT && metrics) {

            try {
                content = ModuleMetricsProcessor.getInstance().buildMoudleMetrics().getBytes("GBK");


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }

        } else if (!SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT && metrics) {
            content = METRICS_ERR_MSG.getBytes();
        } else {

            AbilityDetailProvider provider = new AbilityDetailProvider();
            content = provider.listAbilityDetail(true).toString().getBytes();
        }

        return content;


    }


}

















































