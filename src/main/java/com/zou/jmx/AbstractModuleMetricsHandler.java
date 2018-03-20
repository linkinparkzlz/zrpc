package com.zou.jmx;

import com.zou.config.SystemConfig;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.NotificationBroadcasterSupport;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractModuleMetricsHandler extends NotificationBroadcasterSupport implements ModuleMetricsVisitorMXBean {


    protected List<ModuleMetricsVisitor> visitorList = new CopyOnWriteArrayList<>();
    private static String startTime;
    private final AtomicBoolean locked = new AtomicBoolean(false);

    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    private static final int METRICS_VISITOR_LIST_SIZE = HashModuleMetricsVisitor.getInstance().getHashModuleMetricsVisitorListSize();

    private MetricsTask[] tasks = new MetricsTask[METRICS_VISITOR_LIST_SIZE];

    private boolean aggregationTaskFlag = false;

    private ExecutorService executorService = Executors.newFixedThreadPool(METRICS_VISITOR_LIST_SIZE);


    @Override
    public List<ModuleMetricsVisitor> getModuleMetricsVisitor() {
        return null;
    }

    public AbstractModuleMetricsHandler() {


    }

    @Override
    public void addModuleMetricsVisitor(ModuleMetricsVisitor visitor) {
        visitorList.add(visitor);
    }


    public List<ModuleMetricsVisitor> getMoudleMetricsVisitor() {

        if (SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT) {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MetricsAggregationTask aggregationTask = new MetricsAggregationTask(aggregationTaskFlag, tasks, visitorList, countDownLatch);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(METRICS_VISITOR_LIST_SIZE, aggregationTask);

            for (int i = 0; i < METRICS_VISITOR_LIST_SIZE; i++) {

                tasks[i] = new MetricsTask(cyclicBarrier, HashModuleMetricsVisitor.getInstance().getHashVisitorList().get(i));
                executorService.execute(tasks[i]);

            }

            try {
                visitorList.clear();
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return visitorList;
    }


    public ModuleMetricsVisitor visit(String moduleName, String methodName) {

        try {
            enter();
            return visitCriticalSection(moduleName, methodName);

        } finally {

            exit();
        }

    }


    protected void enter() {

        Thread current = Thread.currentThread();
        waiters.add(current);

        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {

            LockSupport.park(ModuleMetricsVisitor.class);
        }
    }

    protected void exit() {

        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }


    public abstract ModuleMetricsVisitor visitCriticalSection(String moduleName, String methodName);


    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{
                AttributeChangeNotification.ATTRIBUTE_CHANGE
        };

        String name = AttributeChangeNotification.class.getName();
        String description = "the event send zrpc server";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }


    public final static String getStartTime() {

        if (startTime == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startTime = format.format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
        }

        return startTime;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}





































