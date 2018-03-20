package com.test.zou;

import com.zou.exception.InvokeTimeoutException;
import com.zou.services.MultiCalculate;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiCalcParallelRequestThread implements Runnable {


    private CountDownLatch signal;

    private CountDownLatch finish;

    private int taskNumber = 0;

    private MultiCalculate multiCalculate;


    public MultiCalcParallelRequestThread(CountDownLatch signal, CountDownLatch finish, int taskNumber, MultiCalculate multiCalculate) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.multiCalculate = multiCalculate;
    }

    @Override
    public void run() {


        try {
            signal.await();
            int multi = multiCalculate.multi(taskNumber, taskNumber);
            System.out.println(multi);

        } catch (InterruptedException e) {
            Logger.getLogger(MultiCalcParallelRequestThread.class.getName()).log(Level.SEVERE, null, e);
        } catch (InvokeTimeoutException e) {
            System.out.println(e.getMessage());
        } finally {
            finish.countDown();
        }


    }
}






















































