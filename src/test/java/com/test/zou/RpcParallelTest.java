package com.test.zou;

import com.zou.services.AddCalculate;
import com.zou.services.MultiCalculate;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RpcParallelTest {


    public static void parallelAddCalcTask(AddCalculate addCalculate, int parallel) throws InterruptedException {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        CountDownLatch signal = new CountDownLatch(1);

        CountDownLatch finish = new CountDownLatch(parallel);

        for (int index = 0; index < parallel; index++) {


            AddCalcParallelRequestThread addCalcParallelRequestThread = new AddCalcParallelRequestThread(addCalculate, signal, finish, index);

            new Thread(addCalcParallelRequestThread).start();


        }


        signal.countDown();
        finish.await();
        stopWatch.stop();


        String message = String.format("加法计算RPC的调用总共耗时：" + stopWatch.getTime());

        System.out.println(message);


    }


    public static void parallelMultiCalcTask(MultiCalculate multiCalculate, int parallel) throws InterruptedException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CountDownLatch signal = new CountDownLatch(1);

        CountDownLatch finish = new CountDownLatch(parallel);


        for (int index = 0; index < parallel; index++) {


            MultiCalcParallelRequestThread multiCalcParallelRequestThread = new MultiCalcParallelRequestThread(signal, finish, index, multiCalculate);
            new Thread(multiCalcParallelRequestThread).start();
        }


        signal.countDown();
        finish.await();

        stopWatch.stop();

        String message = String.format("乘法计算RPC调用耗时 ：" + stopWatch.getTime());

        System.out.println(message);


    }


    public static void addTask(AddCalculate addCalculate, int parallel) throws InterruptedException {

        RpcParallelTest.parallelAddCalcTask(addCalculate, parallel);
        TimeUnit.MILLISECONDS.sleep(30);
    }


    public static void multiTask(MultiCalculate multiCalculate, int parallel) throws InterruptedException {


        RpcParallelTest.parallelMultiCalcTask(multiCalculate, parallel);

        TimeUnit.MILLISECONDS.sleep(30);

    }


    public static void main(String[] args) throws InterruptedException {

        int parallel = 1000;

        ClassPathXmlApplicationContext classPathXmlApplicationContext
                = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");


        for (int i = 0; i < 1; i++) {

            addTask((AddCalculate) classPathXmlApplicationContext.getBean("addCalc"), parallel);

            multiTask((MultiCalculate) classPathXmlApplicationContext.getBean("multiCalc"), parallel);

            System.out.println("1000轮并发试验完成");

        }

        classPathXmlApplicationContext.destroy();

    }


}
























































