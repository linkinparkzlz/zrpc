package com.zou.netty.initializer;

import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;

import java.util.Map;

public class MessageReceiveInitializeTaskAdapter extends AbstractMessageReceiveInitializeTask {

    public MessageReceiveInitializeTaskAdapter(MessageRequest request, MessageResponse response, Map<String, Object> map) {
        super(request, response, map);
    }

    @Override
    protected void injectInvoke() {

    }

    @Override
    protected void injectSuccessInvoke(long invokeTimespan) {

    }

    @Override
    protected void injectFailInvoke(Throwable error) {

    }

    @Override
    protected void injectFilterInvoke() {

    }

    @Override
    protected void acquire() {

    }

    @Override
    protected void release() {

    }
}
