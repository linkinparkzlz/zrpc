package com.test.zou;

import com.zou.async.AsynchronousCallObject;
import com.zou.async.AsynchronousCallback;
import com.zou.async.AsynchronousInvoker;
import com.zou.exception.InvokeTimeoutException;
import com.zou.services.CostTimeCalculate;
import com.zou.services.pojo.CostTime;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AsynchronousRpcRTimeoutCallTest {


    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        final CostTimeCalculate costTimeCalculate = (CostTimeCalculate) classPathXmlApplicationContext.getBean("costTime");

        AsynchronousInvoker asynchronousInvoker = new AsynchronousInvoker();

        try {
            CostTime costTime = asynchronousInvoker.submit(new AsynchronousCallback<CostTime>() {
                @Override
                public CostTime call() {
                    return costTimeCalculate.busy();
                }
            });

            System.out.println("1 async zrpc call:[" + "result:" + costTime + ", status:[" + ((AsynchronousCallObject) costTime)._getStatus() + "]");
        } catch (InvokeTimeoutException e) {

            System.out.println(e.getMessage());

            classPathXmlApplicationContext.destroy();

            return;

        }

        classPathXmlApplicationContext.destroy();

    }


}





























