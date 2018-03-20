package com.zou.jmx;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ModuleMetricsProcessor {

    private static final ModuleMetricsProcessor INSTANCE = new ModuleMetricsProcessor();
    private MBeanServerConnection connection;
    private final static String TD_BEGIN = "<td>";
    private final static String TD_END = "</td>";

    private final static String TR_BEGIN = "<tr>";
    private final static String TR_END = "</tr>";

    private final static String BR = "</br>";

    private final static String TABLE_BEGIN = "<html><body><div class=\"table-container\"><table border=\"1\">" +
            "<tr><th>模块名称</th><th>方法名称</th><th>调用次数</th><th>调用成功次数</th><th>调用失败次数</th><th>被过滤次数</th><th>方法耗时（毫秒）" +
            "</th><th>方法最大耗时（毫秒）</th><th>方法最小耗时（毫秒）</th><th>方法耗时区间分布</th><th>最后一次失败时间</th><th>最后一次失败堆栈明细</th></tr>";
    private final static String TABLE_END = "</table></body></html>";


    private final static String SUB_TABLE_BEGIN = "<table border=\"1\">";
    private final static String SUB_TABLE_END = "</table>";
    private final static String JMX_METRICS_ATTRBUATE = "ModuleMetricsVisitor";


    public static ModuleMetricsProcessor getInstance() {
        return INSTANCE;
    }

    private ModuleMetricsProcessor() {
        init();
    }


    public void init() {

        ModuleMetricsHandler handler = ModuleMetricsHandler.getInstance();
        connection = handler.connect();

        while (true) {

            if (connection != null) {
                break;
            } else {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                    connection = handler.connect();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    private String buildHistogram(CompositeData data) {

        CompositeDataSupport histogram = (CompositeDataSupport) (data.get("histogram"));

        long[] ranges = (long[]) (histogram.get("ranges"));
        long[] invokeHistogram = (long[]) (data.get("invokeHistogram"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SUB_TABLE_BEGIN);

        int i = 0;
        for (; i < ranges.length; i++) {
            stringBuilder.append(TR_BEGIN + TD_BEGIN + ((i == 0) ? i : ranges[i - 1]) + "~" + ranges[i] + "毫秒的笔数: " + invokeHistogram + BR + TD_END + TR_END);
        }

        stringBuilder.append(TR_BEGIN + TD_BEGIN + "大于等于" + ranges[i - 1] + "毫秒的笔数: " + invokeHistogram[i] + BR + TD_END + TR_END);
        stringBuilder.append(SUB_TABLE_END);
        return stringBuilder.toString();

    }


    public String buildMoudleMetrics() {

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append(TABLE_BEGIN);
        ObjectName name = null;


        try {
            name = new ObjectName(ModuleMetricsHandler.MBEAN_NAME);

        } catch (MalformedObjectNameException e) {
            e.printStackTrace();

        }


        try {

            Object object = connection.getAttribute(name, JMX_METRICS_ATTRBUATE);
            if (object instanceof CompositeData[]) {

                for (CompositeData compositeData : (CompositeData[]) object) {
                    CompositeData data = (CompositeData) compositeData;

                    String moduleName = (String) (data.get("moduleName"));
                    String methodName = (String) (data.get("methodName"));

                    long invokeCount = (Long) (data.get("invokeCount"));
                    long invokeSuccessCount = (Long) (data.get("invokeSuccessCount"));

                    long invokeFailCount = (Long) (data.get("invokeFailCount"));
                    long invokeFilterCount = (Long) (data.get("invokeFilterCount"));

                    long invokeTimespan = (Long) (data.get("invokeTimespan"));

                    long invokeMinTimespan = (Long) (data.get("invokeMinTimespan"));
                    long invokeMaxTimespan = (Long) (data.get("invokeMaxTimespan"));

                    String lastStackTraceDetail = (String) (data.get("lastStackTraceDetail"));
                    String lastErrorTime = (String) (data.get("lastErrorTime"));
                    String distribute = buildHistogram(data);

                    stringBuilder.append(TR_BEGIN);
                    stringBuilder.append(TD_BEGIN + moduleName + TD_END);
                    stringBuilder.append(TD_BEGIN + methodName + TD_END);

                    stringBuilder.append(TD_BEGIN + invokeCount + TD_END);
                    stringBuilder.append(TD_BEGIN + invokeSuccessCount + TD_END);
                    stringBuilder.append(TD_BEGIN + invokeFailCount + TD_END);
                    stringBuilder.append(TD_BEGIN + invokeFilterCount + TD_END);
                    stringBuilder.append(TD_BEGIN + invokeTimespan + TD_END);
                    stringBuilder.append(TD_BEGIN + invokeMinTimespan + TD_END);
                    stringBuilder.append(TD_BEGIN + invokeMaxTimespan + TD_END);
                    stringBuilder.append(TD_BEGIN + distribute + TD_END);

                    stringBuilder.append(TD_BEGIN + (lastErrorTime != null ? lastErrorTime : "") + TD_END);
                    stringBuilder.append(TD_BEGIN + lastStackTraceDetail + TD_END);

                    stringBuilder.append(TR_END);


                }

            }

            stringBuilder.append(TABLE_END);

        } catch (MBeanException e) {

            e.printStackTrace();

        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }


}












































































