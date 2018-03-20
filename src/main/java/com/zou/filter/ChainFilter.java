package com.zou.filter;

import com.zou.bean.MessageRequest;
import com.zou.core.ModuleInvoker;

public interface ChainFilter {

    Object invoke(ModuleInvoker<?> invoker, MessageRequest request) throws Throwable;
}
