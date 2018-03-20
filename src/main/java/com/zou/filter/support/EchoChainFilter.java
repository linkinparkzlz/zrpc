package com.zou.filter.support;

import com.zou.bean.MessageRequest;
import com.zou.core.ModuleInvoker;
import com.zou.filter.ChainFilter;

public class EchoChainFilter implements ChainFilter {

    @Override
    public Object invoke(ModuleInvoker<?> invoker, MessageRequest request) throws Throwable {

        Object object = null;

        try {
            System.out.println("EchoChainFilter##TRACE MESSAGE-ID:" + request.getMessageId());

            object = invoker.invoke(request);
            return object;
        } catch (Throwable throwable) {

            throwable.printStackTrace();
            throw throwable;

        }
    }
}













































