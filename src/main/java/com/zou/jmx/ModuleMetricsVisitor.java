package com.zou.jmx;


import com.alibaba.druid.util.Histogram;

import javax.management.JMException;
import javax.management.openmbean.*;
import java.beans.ConstructorProperties;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class ModuleMetricsVisitor {


    public static final long DEFAULT_INVOKE_MIN_TIMESPAN = 3600 * 1000L;

    private static final String[] THROWABLE_NAMES = {"message", "class", "stackTrace"};

    private static final String[] THROWABLE_DESCRIPTIONS = {"message", "class", "stackTrace"};

    private static final OpenType<?>[] THROWABLE_TYPES = new OpenType<?>[]{SimpleType.STRING, SimpleType.STRING, SimpleType.STRING};

    private static CompositeType THROWABLE_COMPOSITE_TYPE = null;


    private String moduleName;
    private String methodName;
    private volatile long invokeCount = 0L;
    private volatile long invokeSuccessCount = 0L;
    private volatile long invokeFailCount = 0L;
    private volatile long invokeFilterCount = 0L;

    private long invokeTimeSpan = 0L;
    private long invokeMinTimesan = DEFAULT_INVOKE_MIN_TIMESPAN;
    private long invokeMaxTimespan = 0L;
    private long[] invokeHistogram;
    private Exception lastStackTrace;
    private String lastStackTraceDetail;
    private long lastErrorTime;
    private int hashKey = 0;


    private Histogram histogram = new Histogram(TimeUnit.MILLISECONDS, new long[]{1, 10, 100, 1000, 10 * 1000, 100 * 1000, 1000 * 1000});


    private final AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeCountUpdater =
            AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeCount");

    private final AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeSuccessCountUpdater =
            AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeSuccessCount");

    private final AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeFailCounterUpdater =
            AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeFailCount");

    private final AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeFilterCountUpdater =
            AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeFilterCount");


    @ConstructorProperties({"moduleName", "methodName"})
    public ModuleMetricsVisitor(String moduleName, String methodName) {
        this.moduleName = moduleName;
        this.methodName = methodName;
        clear();
    }


    public void clear() {

        lastStackTraceDetail = "";
        invokeTimeSpan = 0L;
        invokeTimeSpan = DEFAULT_INVOKE_MIN_TIMESPAN;
        invokeMaxTimespan = 0L;
        lastErrorTime = 0L;
        lastStackTrace = null;
        invokeCountUpdater.set(this, 0);
        invokeSuccessCountUpdater.set(this, 0);
        invokeFailCounterUpdater.set(this, 0);
        invokeFilterCountUpdater.set(this, 0);
        histogram.reset();


    }

    public void reset() {

        methodName = "";
        methodName = "";
        clear();
    }


    //生成geter和setter方法


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public long getInvokeCount() {
        return this.invokeCountUpdater.get(this);
    }


    public void setInvokeCount(long invokeCount) {
        this.invokeCountUpdater.set(this, invokeCount);
    }


    public long incrementInvokeCount() {
        return this.invokeCountUpdater.incrementAndGet(this);
    }


    public long getInvokeSuccessCount() {
        return this.invokeSuccessCountUpdater.get(this);
    }


    public void setInvokeSuccessCount(long invokeSuccessCount) {
        this.invokeSuccessCountUpdater.set(this, invokeSuccessCount);
    }


    public long incrementInvokeSuccessCount() {
        return this.invokeSuccessCountUpdater.incrementAndGet(this);
    }


    public long getInvokeFailCount() {
        return this.invokeFailCounterUpdater.get(this);
    }

    public void setInvokeFailCount(long invokeFailCount) {
        this.invokeFailCounterUpdater.set(this, invokeFailCount);
    }


    public long incrementInvokeFailCount() {

        return this.invokeFailCounterUpdater.incrementAndGet(this);
    }


    public long getInvokeFilterCount() {
        return this.invokeFilterCountUpdater.get(this);
    }

    public void setInvokeFilterCount(long invokeFilterCount) {
        this.invokeFilterCountUpdater.set(this, invokeFilterCount);
    }


    public long incrementInvokeFilterCount() {
        return this.invokeFilterCountUpdater.incrementAndGet(this);
    }


    public long getInvokeTimeSpan() {
        return invokeTimeSpan;
    }

    public void setInvokeTimeSpan(long invokeTimeSpan) {
        this.invokeTimeSpan = invokeTimeSpan;
    }

    public long getInvokeMinTimesan() {
        return invokeMinTimesan;
    }

    public void setInvokeMinTimesan(long invokeMinTimesan) {
        this.invokeMinTimesan = invokeMinTimesan;
    }

    public long getInvokeMaxTimespan() {
        return invokeMaxTimespan;
    }

    public void setInvokeMaxTimespan(long invokeMaxTimespan) {
        this.invokeMaxTimespan = invokeMaxTimespan;
    }

    public long[] getInvokeHistogram() {
        return invokeHistogram;
    }

    public void setInvokeHistogram(long[] invokeHistogram) {
        this.invokeHistogram = invokeHistogram;
    }

//    public Exception getLastStackTrace() {
//        return lastStackTrace;
//    }

    public String getLastStackTrace() {
        if (lastStackTrace == null) {
            return null;
        }

        StringWriter stringWriter = new StringWriter();
        lastStackTrace.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

//
//    public void setLastStackTrace(Exception lastStackTrace) {
//        this.lastStackTrace = lastStackTrace;
//    }
//

    public String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }


    public void setLastStackTrace(Exception lastStackTrace) {

        this.lastStackTrace = lastStackTrace;
        this.lastStackTraceDetail = getLastStackTrace();
        this.lastErrorTime = System.currentTimeMillis();
    }


    public String getLastStackTraceDetail() {
        return lastStackTraceDetail;
    }

    public void setLastStackTraceDetail(String lastStackTraceDetail) {
        this.lastStackTraceDetail = lastStackTraceDetail;
    }

//    public long getLastErrorTime() {
//        return lastErrorTime;
//    }
//
//    public void setLastErrorTime(long lastErrorTime) {
//        this.lastErrorTime = lastErrorTime;
//    }


    public void setLastErrorTimeLongValue(long lastErrorTime) {
        this.lastErrorTime = lastErrorTime;
    }


    public long getLastErrorTimeLongValue() {

        return lastErrorTime;
    }


    public String getLastErrorTime() {

        if (lastErrorTime <= 0) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(lastErrorTime));
    }


    public static CompositeType getThrowableCompositeType() throws JMException {

        if (THROWABLE_COMPOSITE_TYPE == null) {

            THROWABLE_COMPOSITE_TYPE = new CompositeType("Throwable", "Throwable", THROWABLE_NAMES, THROWABLE_DESCRIPTIONS, THROWABLE_TYPES);
        }

        return THROWABLE_COMPOSITE_TYPE;

    }


    public CompositeData buildErrorCompositeData(Throwable error) throws JMException {

        if (error == null) {
            return null;
        }


        Map<String, Object> map = new HashMap<>(512);

        map.put("class", error.getClass().getName());
        map.put("message", error.getMessage());
        map.put("stackTrace", getStackTrace(error));


        return new CompositeDataSupport(getThrowableCompositeType(), map);

    }


    public int getHashKey() {
        return hashKey;
    }

    public void setHashKey(int hashKey) {
        this.hashKey = hashKey;
    }


    //重写hashCode()方法


    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((methodName == null) ? 0 : moduleName.hashCode());
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());

        return result;
    }


    public Histogram getHistogram() {
        return histogram;
    }

    @Override
    public boolean equals(Object obj) {
        return moduleName.equals(((ModuleMetricsVisitor) obj).methodName) && methodName.equals(((ModuleMetricsVisitor) obj).methodName);
    }


    @Override
    public String toString() {
        String metrics = String.format("<<[moduleName:%s]-[methodName:%s]>> " +
                        "[invokeCount:%d][invokeSuccCount:%d][invokeFilterCount:%d][invokeTimespan:%d]" +
                        "[invokeMinTimespan:%d][invokeMaxTimespan:%d][invokeFailCount:%d][lastErrorTime:%d][lastStackTraceDetail:%s]\n",
                moduleName, methodName, invokeCount, invokeSuccessCount, invokeFilterCount, invokeTimeSpan, invokeMinTimesan,
                invokeMaxTimespan, invokeFailCount, lastErrorTime, lastStackTraceDetail);
        return metrics;
    }
}

























































































