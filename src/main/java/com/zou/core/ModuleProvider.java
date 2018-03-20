package com.zou.core;

public interface ModuleProvider<T> {

    ModuleInvoker<T> getInvoker();

    void destoryInvoker();

}
