package com.zou.core;

import com.zou.bean.MessageRequest;

public interface ModuleInvoker<T> {

    Class<T> getInterface();

    Object invoke(MessageRequest request) throws Throwable;

    void destroy();

}
