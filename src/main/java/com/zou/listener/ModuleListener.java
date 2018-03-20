package com.zou.listener;

import com.zou.bean.MessageRequest;
import com.zou.core.ModuleProvider;

public interface ModuleListener {


    void exported(ModuleProvider<?> provider, MessageRequest request);

    void unExported(ModuleProvider<?> provider, MessageRequest request);


}
