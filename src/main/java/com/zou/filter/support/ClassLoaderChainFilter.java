package com.zou.filter.support;

import com.zou.bean.MessageRequest;
import com.zou.core.ModuleInvoker;
import com.zou.filter.ChainFilter;

public class ClassLoaderChainFilter implements ChainFilter {

    @Override
    public Object invoke(ModuleInvoker<?> invoker, MessageRequest request) throws Throwable {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(invoker.getInterface().getClassLoader());

        Object result = null;

        try {
            result = invoker.invoke(request);
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;

        } finally {
            Thread.currentThread().setContextClassLoader(classLoader);

        }
    }
}




































































