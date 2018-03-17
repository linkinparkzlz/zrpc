package com.zou.netty.initializer;

import com.google.common.reflect.AbstractInvocationHandler;
import com.zou.bean.MessageRequest;
import com.zou.core.MessageCallBack;
import com.zou.netty.messageHandler.MessageSendHandler;
import com.zou.netty.task.RpcServerLoader;

import java.lang.reflect.Method;
import java.util.UUID;

public class MessageSendProxy<T> extends AbstractInvocationHandler {

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {

        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameterValue(args);


        MessageSendHandler messageSendHandler = RpcServerLoader.getInstance().getMessageSendHandler();

        MessageCallBack messageCallBack = messageSendHandler.sendRequest(request);

        return messageCallBack.start();


    }
}


