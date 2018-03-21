package com.zou.spring;

import com.google.common.eventbus.EventBus;
import com.zou.event.ClientStopEvent;
import com.zou.event.ClientStopEventListener;
import com.zou.netty.executor.MessageReceiveExecutor;
import com.zou.netty.executor.MessageSendExecutor;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ZRpcReference implements FactoryBean, InitializingBean, DisposableBean {

    private String interfaceName;
    private String ipAddress;
    private String protocol;
    private EventBus eventBus = new EventBus();

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void destroy() throws Exception {
        eventBus.post(new ClientStopEvent(0));

    }

    @Override
    public Object getObject() throws Exception {
        return MessageSendExecutor.getInstance().execute(getObjectType());
    }

    @Override
    public Class<?> getObjectType() {

        try {
            return this.getClass().getClassLoader().loadClass(interfaceName);

        } catch (ClassNotFoundException e) {
            System.out.println("fail");


        }

        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        MessageSendExecutor.getInstance().setRpcServerLoader(ipAddress, RpcSerializableProtocol.valueOf(protocol));
        ClientStopEventListener listener = new ClientStopEventListener();
        eventBus.register(listener);

    }


}


































































