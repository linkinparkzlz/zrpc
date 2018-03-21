package com.zou.spring;

import com.zou.event.ServerStartEvent;
import com.zou.filter.Filter;
import com.zou.filter.ServiceFilterBinder;
import com.zou.netty.executor.MessageReceiveExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class ZRpcService implements ApplicationContextAware, ApplicationListener {


    private String interfaceName;
    private String ref;
    private String filter;
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
        applicationContext.publishEvent(new ServerStartEvent(new Object()));


    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        ServiceFilterBinder serviceFilterBinder = new ServiceFilterBinder();

        if (StringUtils.isBlank(filter) || !(applicationContext.getBean(filter) instanceof Filter)) {

            serviceFilterBinder.setObject(applicationContext.getBean(ref));
        } else {
            serviceFilterBinder.setObject(applicationContext.getBean(ref));
            serviceFilterBinder.setFilter((Filter) applicationContext.getBean(filter));
        }

        MessageReceiveExecutor.getInstance().getHandlerMap().put(interfaceName, serviceFilterBinder);

    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}


























































