package com.zou.core;

import com.zou.bean.MessageRequest;

public interface Modular {

    <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request);


}
