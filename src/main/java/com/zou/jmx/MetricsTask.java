package com.zou.jmx;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.objectweb.asm.tree.ModuleNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MetricsTask implements Runnable {


    private final CyclicBarrier cyclicBarrier;

    private List<ModuleMetricsVisitor> visitorList;

    private List<ModuleMetricsVisitor> result = new ArrayList<>();


    public MetricsTask(CyclicBarrier cyclicBarrier, List<ModuleMetricsVisitor> visitorList) {
        this.cyclicBarrier = cyclicBarrier;
        this.visitorList = visitorList;
    }

    @Override
    public void run() {

        try {
            cyclicBarrier.await();
            accumulate();
            cyclicBarrier.await();

        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (BrokenBarrierException e) {

            e.printStackTrace();
        }


    }


    private void accumulate() {

        List<ModuleMetricsVisitor> list = visitorList;

        Iterator iterator = new UniqueFilterIterator(list.iterator());

        while (iterator.hasNext()) {
            ModuleMetricsVisitor visitor = (ModuleMetricsVisitor) iterator.next();

            result.add(new ModuleMetricsVisitor(visitor.getModuleName(), visitor.getMethodName()));
        }

        count(list);
    }


    public List<ModuleMetricsVisitor> getResult() {
        return result;
    }

    public void count(List<ModuleMetricsVisitor> list) {

        for (int i = 0; i < result.size(); i++) {

            long invokeCount = 0L;
            long invokeSuccessCount = 0L;
            long invokeFailCount = 0L;
            long invokeFilterCount = 0L;
            long invokeTimespan = 0L;
            long invokeMinTimespan = list.get(0).getInvokeMinTimesan();
            long invokeMaxTimesapn = list.get(0).getInvokeMaxTimespan();

            int length = result.get(i).getHistogram().getRanges().length + 1;

            long[] invokeHistogram = new long[length];
            Arrays.fill(invokeHistogram, 0L);

            String lastStackTraceDetail = "";
            long lastErrorTime = list.get(0).getLastErrorTimeLongValue();


            ModuleMetrics moduleMetrics = new ModuleMetrics();
            moduleMetrics.setInvokeCount(invokeCount);
            moduleMetrics.setInvokeSuccessCount(invokeSuccessCount);
            moduleMetrics.setInvokeFailCount(invokeFailCount);
            moduleMetrics.setInvokeFilterCount(invokeFilterCount);
            moduleMetrics.setInvokeTimespan(invokeTimespan);
            moduleMetrics.setInvokeMinTimespan(invokeMinTimespan);
            moduleMetrics.setInvokeMaxTimesapn(invokeMaxTimesapn);
            moduleMetrics.setInvokeHistogram(invokeHistogram);
            moduleMetrics.setLastStackTraceDetail(lastStackTraceDetail);
            moduleMetrics.setLastErrorTime(lastErrorTime);

            merge(i, list, moduleMetrics);

            result.get(i).setInvokeCount(moduleMetrics.getInvokeCount());
            result.get(i).setInvokeSuccessCount(moduleMetrics.getInvokeSuccessCount());
            result.get(i).setInvokeFailCount(moduleMetrics.getInvokeFailCount());
            result.get(i).setInvokeFilterCount(moduleMetrics.getInvokeFilterCount());
            result.get(i).setInvokeTimeSpan(moduleMetrics.getInvokeTimespan());
            result.get(i).setInvokeMinTimesan(moduleMetrics.getInvokeMinTimespan());
            result.get(i).setInvokeMaxTimespan(moduleMetrics.getInvokeMaxTimesapn());
            result.get(i).setInvokeHistogram(moduleMetrics.getInvokeHistogram());


            if (moduleMetrics.getLastErrorTime() > 0) {

                result.get(i).setLastErrorTimeLongValue(moduleMetrics.getLastErrorTime());
                result.get(i).setLastStackTraceDetail(moduleMetrics.getLastStackTraceDetail());
            }

        }


    }


    public void merge(int index, List<ModuleMetricsVisitor> list, ModuleMetrics moduleMetrics) {

        long invokeCount = moduleMetrics.getInvokeCount();
        long invokeSuccessCount = moduleMetrics.getInvokeSuccessCount();
        long invokeFailCount = moduleMetrics.getInvokeFailCount();
        long invokeFilterCount = moduleMetrics.getInvokeFilterCount();
        long invokeTimespan = moduleMetrics.getInvokeTimespan();
        long invokeMinTimespan = moduleMetrics.getInvokeMinTimespan();
        long invokeMaxTimesapn = moduleMetrics.getInvokeMaxTimesapn();
        long[] invokeHistogram = moduleMetrics.getInvokeHistogram();
        String lastStackTraceDetail = moduleMetrics.getLastStackTraceDetail();
        long lastErrorTime = moduleMetrics.getLastErrorTime();


        for (int i = 0; i < list.size(); i++) {

            boolean find = equals(result.get(index).getMethodName(), list.get(i).getModuleName(), result.get(index).getMethodName(), list.get(i).getMethodName());

            if (find) {
                invokeCount += list.get(i).getInvokeCount();
                invokeSuccessCount += list.get(i).getInvokeSuccessCount();
                invokeFailCount += list.get(i).getInvokeFailCount();
                invokeFilterCount += list.get(i).getInvokeFilterCount();
                long timespan = list.get(i).getInvokeMaxTimespan();

                if (timespan > 0) {
                    invokeTimespan = timespan;
                }

                long minTimespan = list.get(i).getInvokeMinTimesan();
                long maxTimespan = list.get(i).getInvokeMaxTimespan();

                if (minTimespan < invokeMinTimespan) {
                    invokeMaxTimesapn = minTimespan;
                }

                if (maxTimespan > invokeMaxTimesapn) {
                    invokeMaxTimesapn = maxTimespan;
                }

                for (int j = 0; j < invokeHistogram.length; j++) {

                    invokeHistogram[j] += list.get(i).getHistogram().toArray()[j];
                }

                long fail = list.get(i).getInvokeFailCount();
                if (fail > 0) {

                    long lastTime = list.get(i).getLastErrorTimeLongValue();

                    if (lastTime > lastErrorTime) {
                        lastErrorTime = lastTime;
                        lastStackTraceDetail = list.get(i).getLastStackTraceDetail();
                    }
                }
            }
        }

        moduleMetrics.setInvokeCount(invokeCount);
        moduleMetrics.setInvokeSuccessCount(invokeSuccessCount);
        moduleMetrics.setInvokeFailCount(invokeFailCount);
        moduleMetrics.setInvokeFilterCount(invokeFilterCount);
        moduleMetrics.setInvokeTimespan(invokeTimespan);
        moduleMetrics.setInvokeMinTimespan(invokeMinTimespan);
        moduleMetrics.setInvokeMaxTimesapn(invokeMaxTimesapn);
        moduleMetrics.setInvokeHistogram(invokeHistogram);
        moduleMetrics.setLastStackTraceDetail(lastStackTraceDetail);
        moduleMetrics.setLastErrorTime(lastErrorTime);


    }


    private boolean equals(String srcModuleName, String destModuleName, String srcMethodName, String destMethodName) {
        return srcModuleName.equals(destModuleName) && srcMethodName.equals(destMethodName);
    }

    private class ModuleMetrics {


        private long invokeCount;
        private long invokeSuccessCount;
        private long invokeFailCount;
        private long invokeFilterCount;
        private long invokeTimespan;
        private long invokeMinTimespan;
        private long invokeMaxTimesapn;
        private long[] invokeHistogram;
        private String lastStackTraceDetail;
        private long lastErrorTime;


        public long getInvokeCount() {
            return invokeCount;
        }

        public void setInvokeCount(long invokeCount) {
            this.invokeCount = invokeCount;
        }

        public long getInvokeSuccessCount() {
            return invokeSuccessCount;
        }

        public void setInvokeSuccessCount(long invokeSuccessCount) {
            this.invokeSuccessCount = invokeSuccessCount;
        }

        public long getInvokeFailCount() {
            return invokeFailCount;
        }

        public void setInvokeFailCount(long invokeFailCount) {
            this.invokeFailCount = invokeFailCount;
        }

        public long getInvokeFilterCount() {
            return invokeFilterCount;
        }

        public void setInvokeFilterCount(long invokeFilterCount) {
            this.invokeFilterCount = invokeFilterCount;
        }

        public long getInvokeTimespan() {
            return invokeTimespan;
        }

        public void setInvokeTimespan(long invokeTimespan) {
            this.invokeTimespan = invokeTimespan;
        }

        public long getInvokeMinTimespan() {
            return invokeMinTimespan;
        }

        public void setInvokeMinTimespan(long invokeMinTimespan) {
            this.invokeMinTimespan = invokeMinTimespan;
        }

        public long getInvokeMaxTimesapn() {
            return invokeMaxTimesapn;
        }

        public void setInvokeMaxTimesapn(long invokeMaxTimesapn) {
            this.invokeMaxTimesapn = invokeMaxTimesapn;
        }

        public long[] getInvokeHistogram() {
            return invokeHistogram;
        }

        public void setInvokeHistogram(long[] invokeHistogram) {
            this.invokeHistogram = invokeHistogram;
        }

        public String getLastStackTraceDetail() {
            return lastStackTraceDetail;
        }

        public void setLastStackTraceDetail(String lastStackTraceDetail) {
            this.lastStackTraceDetail = lastStackTraceDetail;
        }

        public long getLastErrorTime() {
            return lastErrorTime;
        }

        public void setLastErrorTime(long lastErrorTime) {
            this.lastErrorTime = lastErrorTime;
        }
    }


}









































































