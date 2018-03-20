package com.zou.listener;

import com.zou.bean.MessageRequest;
import com.zou.core.ModuleInvoker;
import com.zou.core.ModuleProvider;

import java.util.List;

public class ModuleProviderWarper<T> implements ModuleProvider<T> {


    private ModuleProvider<T> provider;
    private MessageRequest request;
    private List<ModuleListener> listeners;


    public ModuleProviderWarper(ModuleProvider<T> provider, List<ModuleListener> listeners, MessageRequest request) {

        if (provider == null) {
            throw new IllegalArgumentException("provider is null");
        }

        this.provider = provider;
        this.listeners = listeners;
        this.request = request;

        if (listeners != null && listeners.size() > 0) {
            RuntimeException exception = null;

            for (ModuleListener listener : listeners) {
                if (listener != null) {
                    try {
                        listener.exported(this, request);
                    } catch (RuntimeException e) {
                        exception = e;
                    }
                }
            }

            if (exception != null) {
                throw exception;
            }
        }

    }


    @Override
    public ModuleInvoker<T> getInvoker() {
        return provider.getInvoker();
    }

    @Override
    public void destoryInvoker() {

        try {
            provider.destoryInvoker();
        } finally {

            if (listeners != null && listeners.size() > 0) {

                RuntimeException exception = null;

                for (ModuleListener listener : listeners) {

                    if (listener != null) {
                        try {
                            listener.unExported(this, request);
                        } catch (RuntimeException e) {
                            exception = e;
                        }
                    }
                }

                if (exception != null) {
                    throw exception;
                }
            }

        }
    }
}


































































































