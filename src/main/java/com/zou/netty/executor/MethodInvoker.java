package com.zou.netty.executor;

import com.zou.bean.MessageRequest;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.time.StopWatch;

public class MethodInvoker {

    private Object serviceBean;
    private StopWatch stopWatch = new StopWatch();

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }


    public Object invoke(MessageRequest request) throws Throwable {

        String methodName = request.getMethodName();

        Object[] paramaters = request.getParameterValue();

        stopWatch.reset();
        stopWatch.start();

        Object object = MethodUtils.invokeMethod(serviceBean, methodName, paramaters);
        stopWatch.stop();
        return object;

    }

    public long getInvokeTimespan() {
        return stopWatch.getTime();
    }

}

























































