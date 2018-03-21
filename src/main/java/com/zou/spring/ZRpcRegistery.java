package com.zou.spring;

import com.zou.bean.ThreadPoolMonitorProvider;
import com.zou.config.SystemConfig;
import com.zou.jmx.HashModuleMetricsVisitor;
import com.zou.jmx.ModuleMetricsHandler;
import com.zou.netty.executor.MessageReceiveExecutor;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ZRpcRegistery implements InitializingBean, DisposableBean {


    private String ipAddress;
    private String protocol;
    private String port;
    private AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext();


    @Override
    public void destroy() throws Exception {

        MessageReceiveExecutor.getInstance().stop();

        if (SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT) {
            ModuleMetricsHandler handler = ModuleMetricsHandler.getInstance();

            handler.stop();
        }


    }

    @Override
    public void afterPropertiesSet() throws Exception {

        MessageReceiveExecutor messageReceiveExecutor = MessageReceiveExecutor.getInstance();
        messageReceiveExecutor.setServerIpAddress(ipAddress);
       // messageReceiveExecutor.setPort(Integer.parseInt(port));
        messageReceiveExecutor.setPort(Integer.parseInt(port));
        messageReceiveExecutor.setSerializableProtocol(Enum.valueOf(RpcSerializableProtocol.class, protocol));


        if (SystemConfig.isMonitorServerSupport()) {

            configApplicationContext.register(ThreadPoolMonitorProvider.class);
            configApplicationContext.refresh();
        }

        messageReceiveExecutor.start();

        if (SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT) {

            HashModuleMetricsVisitor visitor = HashModuleMetricsVisitor.getInstance();
            visitor.signal();
            ModuleMetricsHandler handler = ModuleMetricsHandler.getInstance();
            handler.start();

        }


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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}





















































