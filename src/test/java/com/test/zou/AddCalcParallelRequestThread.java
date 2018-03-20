package com.test.zou;

import com.zou.exception.InvokeTimeoutException;
import com.zou.services.AddCalculate;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCalcParallelRequestThread implements Runnable {

    private CountDownLatch signal;

    private CountDownLatch finish;

    private int taskNumber;

    private AddCalculate addCalculate;


    public AddCalcParallelRequestThread( AddCalculate addCalculate,CountDownLatch signal, CountDownLatch finish, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.addCalculate = addCalculate;
    }

    @Override
    public void run() {


        try {

            signal.await();

            int add = addCalculate.add(taskNumber, taskNumber);

            System.out.println(add);
        } catch (InterruptedException e) {

            Logger.getLogger(AddCalcParallelRequestThread.class.getName()).log(Level.SEVERE, null, e);

        } catch (InvokeTimeoutException e) {
            System.out.println(e.getMessage());
        } finally {
            finish.countDown();
        }

    }
}






























