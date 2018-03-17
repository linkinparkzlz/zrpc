package com.zou.netty.initializer;

import com.dyuproject.protostuff.parser.Field;
import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.config.SystemConfig;
import com.zou.core.Modular;
import com.zou.core.ModuleInvoker;
import com.zou.core.ModuleProvider;
import com.zou.netty.executor.MethodInvoker;
import com.zou.netty.executor.MethodProxyAdvisor;
import com.zou.spring.BeanFactoryHelper;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractMessageReceiveInitializeTask implements Callable<Boolean> {

    protected MessageRequest request = null;
    protected MessageResponse response = null;
    protected Map<String, Object> map = null;

    protected static final String METHOD_MAPPED_NAME = "invoke";
    protected boolean returnNotNull = true;
    protected long invokeTimespan;
    protected Modular modular = BeanFactoryHelper.getBean("modular");


    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    public MessageResponse getResponse() {
        return response;
    }

    public void setResponse(MessageResponse response) {
        this.response = response;
    }

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    public AbstractMessageReceiveInitializeTask(MessageRequest request, MessageResponse response, Map<String, Object> map) {
        this.request = request;
        this.response = response;
        this.map = map;
    }


    protected abstract void injectInvoke();

    protected abstract void injectSuccessInvoke(long invokeTimespan);

    protected abstract void injectFailInvoke(Throwable error);

    protected abstract void injectFilterInvoke();

    protected abstract void acquire();

    protected abstract void release();


    private Object reflect(MessageRequest request) throws Throwable {

        ProxyFactory proxyFactory = new ProxyFactory(new MethodInvoker());

        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();

        advisor.setMappedName(METHOD_MAPPED_NAME);
        advisor.setAdvice(new MethodProxyAdvisor(map));

        proxyFactory.addAdvisor(advisor);
        MethodInvoker methodInvoker = (MethodInvoker) proxyFactory.getProxy();

        Object object = invoke(methodInvoker, request);
        invokeTimespan = methodInvoker.getInvokeTimespan();

        setReturnNotNull(((MethodProxyAdvisor) advisor.getAdvice()).isReturnNotNull());

        return object;


    }


    public String getStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace();
        new PrintWriter(stringWriter);

        return stringWriter.toString();
    }


    @Override
    public Boolean call() {
        try {

            acquire();
            response.setMessageId(request.getMessageId());
            injectInvoke();

            Object object = reflect(request);
            boolean isInvokeSuccess = ((returnNotNull && object != null) || !returnNotNull);

            if (isInvokeSuccess) {

                response.setResult(object);
                response.setError("");
                response.setReturnNotNull(returnNotNull);

                injectSuccessInvoke(invokeTimespan);
            }else {

                System.err.println(SystemConfig.FILTER_RESPONSE_MSG);
                response.setResult(null);
                response.setError(SystemConfig.FILTER_RESPONSE_MSG);
                injectFilterInvoke();
            }
            return Boolean.TRUE;


        } catch (Throwable t) {

            response.setError(getStackTrace(t));
            t.printStackTrace();
            System.err.printf("Rpc server  invoke error");
            injectFailInvoke(t);
            return Boolean.FALSE;

        } finally {

            release();
        }
    }


    private Object invoke(MethodInvoker methodInvoker, MessageRequest request) throws Throwable {

        if (modular != null) {

            ModuleProvider provider = modular.invoke(new ModuleInvoker() {

                @Override
                public Class getInterface() {
                    return methodInvoker.getClass().getInterfaces()[0];
                }

                @Override
                public Object invoke(MessageRequest request) throws Throwable {
                    return methodInvoker.invoke(request);
                }

                @Override
                public void destroy() {

                }
            }, request);

            return provider.getInvoker().invoke(request);
        } else {
            return methodInvoker.invoke(request);
        }

    }


}








































































