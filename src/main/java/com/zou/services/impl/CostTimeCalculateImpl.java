package com.zou.services.impl;

import com.zou.services.CostTimeCalculate;
import com.zou.services.pojo.CostTime;

public class CostTimeCalculateImpl implements CostTimeCalculate {

    @Override
    public CostTime calculate() {

        CostTime elapse = new CostTime();

        try {

            long start = 0, end = 0;
            start = System.currentTimeMillis();

            Thread.sleep(3000L);
            end = System.currentTimeMillis();

            long interval = end - start;

            elapse.setElapse(interval);

            elapse.setDetail("cost time operate success");

            System.out.println("calculate time:" + interval);

            return elapse;

        } catch (InterruptedException e) {

            e.printStackTrace();
            elapse.setDetail("cost tiem operate fail");

            return elapse;

        }


    }

    @Override
    public CostTime busy() {

        CostTime elapse = new CostTime();

        try {

            long start = 0, end = 0;
            start = System.currentTimeMillis();

            Thread.sleep(35 * 1000L);
            end = System.currentTimeMillis();

            long interval = end - start;

            elapse.setElapse(interval);

            elapse.setDetail("busy now");

            System.out.println("calculate time:" + interval);

            return elapse;

        } catch (InterruptedException e) {

            e.printStackTrace();
            elapse.setDetail("handle  error  now");

            return elapse;

        }


    }
}







































































































