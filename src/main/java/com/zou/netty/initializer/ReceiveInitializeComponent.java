package com.zou.netty.initializer;

import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.config.SystemConfig;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 消息接收初始化组件
 */
public class ReceiveInitializeComponent {

    private MessageRequest request;
    private MessageResponse response;

    private Map<String, Object> map;

    private boolean isMetrics = SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT;
    private boolean jmxMetricsHash = SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT;


    public ReceiveInitializeComponent(MessageRequest request, MessageResponse response, Map<String, Object> map) {

        this.request = request;
        this.response = response;
        this.map = map;

    }

    public Callable<Boolean> getTask() {
        return isMetrics ? getMetricsTask() : new MessageReceiveInitializeTaskAdapter(request, response, map);
    }


    private Callable<Boolean> getMetricsTask() {

        return jmxMetricsHash ? new HashMessageReceiveInitializeTask(request, response, map) : new HashMessageReceiveInitializeTask(request, response, map);
    }


}















































