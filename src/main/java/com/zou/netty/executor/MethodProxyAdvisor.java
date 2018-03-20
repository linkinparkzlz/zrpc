package com.zou.netty.executor;

import com.zou.bean.MessageRequest;
import com.zou.filter.Filter;
import com.zou.filter.ServiceFilterBinder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodProxyAdvisor implements MethodInterceptor {

    private Map<String, Object> map;
    private boolean returnNotNull = true;

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }


    public MethodProxyAdvisor(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {


        Object[] paramters = invocation.getArguments();
        if (paramters.length <= 0) {
            return null;
        }

        MessageRequest request = (MessageRequest) paramters[0];

        String className = request.getClassName();
        Object serviceBean = map.get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameterValue();

        boolean existFilter = ServiceFilterBinder.class.isAssignableFrom(serviceBean.getClass());
        ((MethodInvoker) invocation.getThis()).setServiceBean(existFilter ? ((ServiceFilterBinder) serviceBean).getObject() : serviceBean);


        if (existFilter) {

            ServiceFilterBinder processors = (ServiceFilterBinder) serviceBean;

            if (processors.getFilter() != null) {

                Filter filter = processors.getFilter();

                Object[] args = ArrayUtils.nullToEmpty(parameters);

                Class<?>[] parameterTypes = ClassUtils.toClass(args);

                Method method = MethodUtils.getMatchingAccessibleMethod(processors.getObject().getClass(), methodName, parameterTypes);

                if (filter.before(method, processors.getObject(), parameters)) {

                    Object object = invocation.proceed();
                    filter.after(method, processors.getObject(), parameters);
                    setReturnNotNull(object != null);
                    return object;
                } else {
                    return null;
                }

            }

        }


        Object object = invocation.proceed();
        setReturnNotNull(object != null);
        return object;


    }
}




















































































