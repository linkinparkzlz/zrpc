package com.zou.jmx;

import com.zou.config.SystemConfig;
import com.zou.netty.executor.MessageReceiveExecutor;
import com.zou.parallel.SemaphoreWrapper;
import com.zou.parallel.AbstractDaemonThread;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang3.StringUtils;

import javax.management.*;
import javax.management.remote.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class ModuleMetricsHandler extends AbstractModuleMetricsHandler {

    public static final String MBEAN_NAME = "com.zou:type=ModuleMetricsHandler";

    public final static int MODULE_METRICS_JMX_PORT = 1098;
    private String moduleMetricsJmxUrl = "";
    private Semaphore semaphore = new Semaphore(0);

    private SemaphoreWrapper semaphoreWrapper = new SemaphoreWrapper(semaphore);

    private static final ModuleMetricsHandler INSTANCE = new ModuleMetricsHandler();

    private MBeanServerConnection connection;

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private ModuleMetricsListener listener = new ModuleMetricsListener();


    public static ModuleMetricsHandler getInstance() {
        return INSTANCE;
    }

    public ModuleMetricsHandler() {
        super();
    }

    @Override
    public List<ModuleMetricsVisitor> getModuleMetricsVisitor() {
        return super.getModuleMetricsVisitor();
    }


    public CountDownLatch getLatch() {
        return countDownLatch;
    }


    @Override
    public ModuleMetricsVisitor visitCriticalSection(String moduleName, String methodName) {

        final String method = methodName.trim();
        final String module = moduleName.trim();

        Iterator iterator = new FilterIterator(visitorList.iterator(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                String statModuleName = ((ModuleMetricsVisitor) object).getModuleName();

                String statMethodName = ((ModuleMetricsVisitor) object).getMethodName();

                return statModuleName.compareTo(module) == 0 && statMethodName.compareTo(method) == 0;
            }
        });


        ModuleMetricsVisitor visitor = null;

        while (iterator.hasNext()) {
            visitor = (ModuleMetricsVisitor) iterator.next();
            break;
        }

        if (visitor != null) {
            return visitor;
        } else {
            visitor = new ModuleMetricsVisitor(module, method);
            addModuleMetricsVisitor(visitor);
            return visitor;
        }


    }


    public void start() {

        new AbstractDaemonThread() {
            @Override
            public String getDeamonThreadName() {
                return ModuleMetricsHandler.class.getSimpleName();
            }


            @Override
            public void run() {
                MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

                try {

                    countDownLatch.await();
                    LocateRegistry.createRegistry(MODULE_METRICS_JMX_PORT);
                    MessageReceiveExecutor messageReceiveExecutor = MessageReceiveExecutor.getInstance();
                    String ipAddress = StringUtils.isNotEmpty(messageReceiveExecutor.getServerIpAddress()) ?
                            StringUtils.substringBeforeLast(messageReceiveExecutor.getServerIpAddress(), SystemConfig.DELIMITER) : "localhost";
                    moduleMetricsJmxUrl = "service:jmx:rmi:///jndi/rmi://" + ipAddress + ":" + MODULE_METRICS_JMX_PORT + "/ZRpcServer";

                    JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
                    JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mBeanServer);

                    ObjectName name = new ObjectName(MBEAN_NAME);

                    mBeanServer.registerMBean(ModuleMetricsHandler.this, name);
                    mBeanServer.addNotificationListener(name, listener, null, null);
                    jmxConnectorServer.start();

                    semaphoreWrapper.release();

                    System.out.printf("zrpc服务器已经成功启动!\njmx-url: [%s]\n\n", moduleMetricsJmxUrl);


                } catch (RemoteException e) {

                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MBeanRegistrationException e) {
                    e.printStackTrace();
                } catch (InstanceAlreadyExistsException e) {
                    e.printStackTrace();
                } catch (NotCompliantMBeanException e) {
                    e.printStackTrace();
                } catch (MalformedObjectNameException e) {
                    e.printStackTrace();
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }


    public void stop() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {

            ObjectName name = new ObjectName(MBEAN_NAME);
            mBeanServer.unregisterMBean(name);

            ExecutorService executorService = getExecutorService();
            executorService.shutdown();
            while (!executorService.isTerminated()) {

            }
        } catch (MalformedObjectNameException e) {

            e.printStackTrace();


        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }

    }


    public MBeanServerConnection connect() {

        try {

            if (!semaphoreWrapper.isRelease()) {
                semaphoreWrapper.acquire();
            }

            JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);

            JMXConnector jmxConnector = JMXConnectorFactory.connect(url, null);

            connection = jmxConnector.getMBeanServerConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }


    public MBeanServerConnection getConnection() {
        return connection;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}































































