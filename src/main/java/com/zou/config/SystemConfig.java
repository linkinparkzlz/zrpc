package com.zou.config;

/**
 *
 *
 */
public class SystemConfig {

    public static final int SERIALIZABLE_POOL_MAX_TOTAL = 500;
    public static final int SERIALIZABLE_POOL_MIN_IDLE = 10;
    public static final int SERIALIZABLE_POOL_MAX_WAIT_MILLIS = 5000;
    public static final int SERIALIZABLE_POOL_MIN_EVICTABLE_TIME_MILLIS = 600000;


    public static final int SYSTEM_PROPERTY_JMX_INVOKE_METRICS = Integer.getInteger("zrpc.jmx.invoke.metrics", 1);
    public static final boolean SYSTEM_PROPERTY_JMX_METRICS_SUPPORT = SystemConfig.SYSTEM_PROPERTY_JMX_INVOKE_METRICS != 0;


    public static final int SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS = Integer.getInteger("zrpc.jmx.metrics.hash.nums", 8);
    public static final boolean SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT = SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS != 1;


    public static final String FILTER_RESPONSE_MSG = "Illgal, zrpc  server  refused to responsed!";

    public static final int SYSTEM_PROPERTY_JMX_METRICS_LOCK_FAIR = Integer.getInteger("zrpc.jmx.metrics.lock.fair", 0);

    public static final long SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT = Long.getLong("zrpc.default.msg.timeout", 30 * 1000L);


    public static final String TIMEOUT_RESPONSE_MSG = "Timeout request,zrpc server request timeout!";

    public static final String DELIMITER = ":";

    public static final int SYSTEM_PROPERTY_PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());

    public static final int SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS = Integer.getInteger("zrpc.default.thread.nums", 16);

    public static final int SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS = Integer.getInteger("zrpc.default.queue.nums", -1);

    public static final String SYSTEM_PROPRYTY_THREADPOOL_REJECTEDPOOL_POLICY_ATTRIBUATE = "zrpc.parallel.rejected.policy";


    public static final String SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTRBUATE = "zrpc.parallel.queue";

    public static final String RPC_COMPLIER_SPI_ATTRIBUATE = "com.zou.compiler.AccessAdaptive";

    public static final String RPC_ABILITY_DETAIL_SPI_ATTRIBUTE = "com.zou.core.AbilityDetail";

    public static final int IP_ADDRESS_OPRT_ARRAY_LENGTH = 2;


    private static boolean monitorServerSupport = false;

    public static boolean isMonitorServerSupport() {
        return monitorServerSupport;
    }

    public static void setMonitorServerSupport(boolean monitorServerSupport) {
        SystemConfig.monitorServerSupport = monitorServerSupport;
    }
}
































































