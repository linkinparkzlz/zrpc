package com.zou.listener;

import com.zou.bean.MessageRequest;
import com.zou.core.Modular;
import com.zou.core.ModuleInvoker;
import com.zou.core.ModuleProvider;

import java.util.Collections;
import java.util.List;

public class ModuleListennerChainWrapper implements Modular {


    private Modular modular;
    private List<ModuleListener> listeners;

    public ModuleListennerChainWrapper(Modular modular) {
        if (modular == null) {
            throw new IllegalArgumentException("module  is  null");
        }
        this.modular = modular;
    }

    @Override
    public <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request) {
        return new ModuleProviderWarper(modular.invoke(invoker, request), Collections.unmodifiableList(listeners), request);
    }

    public List<ModuleListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ModuleListener> listeners) {
        this.listeners = listeners;
    }
}







































