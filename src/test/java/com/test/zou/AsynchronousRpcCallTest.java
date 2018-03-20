package com.test.zou;

import com.zou.async.AsynchronousCallObject;
import com.zou.async.AsynchronousCallback;
import com.zou.async.AsynchronousInvoker;
import com.zou.services.CostTimeCalculate;
import com.zou.services.pojo.CostTime;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AsynchronousRpcCallTest {


    public static void main(String[] args) {


        ClassPathXmlApplicationContext classPathXmlApplicationContext
                = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        final CostTimeCalculate costTimeCalculate = (CostTimeCalculate) classPathXmlApplicationContext.getBean("costTime");

        long start = 0;
        long end = 0;


        start = System.currentTimeMillis();

        AsynchronousInvoker asynchronousInvoker = new AsynchronousInvoker();
        CostTime costTime = asynchronousInvoker.submit(() -> costTimeCalculate.calculate());

        CostTime costTime1 = asynchronousInvoker.submit(() -> costTimeCalculate.calculate());

        CostTime costTime2 = asynchronousInvoker.submit(() -> costTimeCalculate.calculate());

        System.out.println("1 async nettyrpc call:[" + "result:" + costTime + ", status:[" + ((AsynchronousCallObject) costTime)._getStatus() + "]");
        System.out.println("2 async nettyrpc call:[" + "result:" + costTime1 + ", status:[" + ((AsynchronousCallObject) costTime1)._getStatus() + "]");
        System.out.println("3 async nettyrpc call:[" + "result:" + costTime2 + ", status:[" + ((AsynchronousCallObject) costTime2)._getStatus() + "]");


        end = System.currentTimeMillis();

        System.out.println("异步RPC调用计算时间： " + (end - start));

        classPathXmlApplicationContext.destroy();
    }


}















































