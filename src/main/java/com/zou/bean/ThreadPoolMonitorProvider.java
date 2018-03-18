package com.zou.bean;


import com.zou.jmx.ThreadPoolStatus;
import com.zou.netty.executor.MessageReceiveExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.*;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

import javax.management.*;
import java.io.IOException;

@Configuration
@EnableMBeanExport
@ComponentScan("com.zou.bean")
public class ThreadPoolMonitorProvider {

    public static final String DELIMITER = ":";
    public static final String JMX_POOL_SIZE_METHOD = "setPoolSize";
    public static final String JMX_ACTIVE_COUNT_METHOD = "setActiveCount";
    public static final String JMX_CORE_POOL_SIZE_METHOD = "setCorePoolSize";
    public static final String JMX_MAXINUM_POOL_SIZE_METHOD = "setMaxinumPoolSize";
    public static final String JMX_LARGEST_POOL_SIZE_METHOD = "setLargestPoolSize";
    public static final String JMX_TASK_COUNT_METHOD = "setTaskCount";
    public static final String JMX_COMPLETED_TASK_COUNT_METHOD = "setCompletedTaskCount";
    public static String url;


    @Bean
    public ThreadPoolStatus threadPoolStatus() {
        return new ThreadPoolStatus();
    }

    @Bean
    public MBeanServerFactoryBean mBeanServerFactoryBean() {
        return new MBeanServerFactoryBean();
    }

    @Bean
    public RmiRegistryFactoryBean registry() {
        return new RmiRegistryFactoryBean();
    }

    @Bean
    @DependsOn("registry")
    public ConnectorServerFactoryBean connectorServerFactoryBean() throws MalformedObjectNameException {

        MessageReceiveExecutor messageReceiveExecutor = MessageReceiveExecutor.getInstance();

        String ipAddress = StringUtils.isNotEmpty(messageReceiveExecutor.getServerIpAddress()) ? StringUtils.substringBefore(messageReceiveExecutor.getServerIpAddress(), DELIMITER) : "localhost";

        url = "service:jmx:rmi://" + ipAddress + "/jndi/rmi://" + ipAddress + ":1099/zrpcstatus";
        System.out.println("zrpc jmx monitorUrl : [" + url + "]");
        ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
        connectorServerFactoryBean.setObjectName("connector:name=rmi");
        connectorServerFactoryBean.setServiceUrl(url);
        return connectorServerFactoryBean;

    }


    public static void monitor(ThreadPoolStatus status) throws IOException, MalformedObjectNameException, ReflectionException, MBeanException, InstanceNotFoundException {


        MBeanServerConnectionFactoryBean mBeanServerConnectionFactoryBean = new MBeanServerConnectionFactoryBean();
        mBeanServerConnectionFactoryBean.setServiceUrl(url);
        mBeanServerConnectionFactoryBean.afterPropertiesSet();

        MBeanServerConnection connection = mBeanServerConnectionFactoryBean.getObject();
        ObjectName objectName = new ObjectName("com.zou.jmx:name=threadPoolStatus,type=ThreadPoolStatus");

        connection.invoke(objectName, JMX_POOL_SIZE_METHOD, new Object[]{status.getPoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_ACTIVE_COUNT_METHOD, new Object[]{status.getActiveCount()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_CORE_POOL_SIZE_METHOD, new Object[]{status.getCorePoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_MAXINUM_POOL_SIZE_METHOD, new Object[]{status.getMaxinumPoolSize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_LARGEST_POOL_SIZE_METHOD, new Object[]{status.getLargestPoolsize()}, new String[]{int.class.getName()});
        connection.invoke(objectName, JMX_TASK_COUNT_METHOD, new Object[]{status.getTaskCount()}, new String[]{long.class.getName()});
        connection.invoke(objectName, JMX_COMPLETED_TASK_COUNT_METHOD, new Object[]{status.getCompletedTaskCount()}, new String[]{long.class.getName()});


    }


}







































































































