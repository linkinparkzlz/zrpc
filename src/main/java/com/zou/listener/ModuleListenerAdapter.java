package com.zou.listener;

import com.zou.bean.MessageRequest;
import com.zou.core.ModuleProvider;
import com.zou.jmx.ModuleMetricsListener;
import org.apache.commons.lang3.StringUtils;

public class ModuleListenerAdapter implements ModuleListener {

    @Override
    public void exported(ModuleProvider<?> provider, MessageRequest request) {

        System.out.println(StringUtils.center("[ModuleListenerAdapter##exported]", 48, "*"));

    }

    @Override
    public void unExported(ModuleProvider<?> provider, MessageRequest request) {

        System.out.println(StringUtils.center("[ModuleListenerAdapter##unExported]", 48, "*"));

    }
}










































































