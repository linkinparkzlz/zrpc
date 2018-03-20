package com.zou.jmx;

import com.zou.config.SystemConfig;
import com.zou.core.reflection.ReflectionHelper;
import com.zou.netty.executor.MessageReceiveExecutor;

import java.util.*;

public class HashModuleMetricsVisitor {
    private List<List<ModuleMetricsVisitor>> hashVisitorList = new ArrayList<>();

    private static final HashModuleMetricsVisitor INSTANCE = new HashModuleMetricsVisitor();

    private HashModuleMetricsVisitor() {
        init();
    }

    public static HashModuleMetricsVisitor getInstance() {
        return INSTANCE;
    }

    public int getHashModuleMetricsVisitorListSize() {
        return hashVisitorList.size();
    }

    public void init() {


        Map<String, Object> map = MessageReceiveExecutor.getInstance().getHandlerMap();

        ReflectionHelper helper = new ReflectionHelper();
        Set<String> set = (Set<String>) map.keySet();

        Iterator<String> iterator = set.iterator();

        String key;

        while (iterator.hasNext()) {

            key = iterator.next();

            try {
                List<String> list = helper.getClassMethodSingature(Class.forName(key));

                for (String signature : list) {

                    List<ModuleMetricsVisitor> visitorList = new ArrayList<>();

                    for (int i = 0; i < SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS; i++) {

                        ModuleMetricsVisitor visitor = new ModuleMetricsVisitor(key, signature);
                        visitor.setHashKey(i);
                        visitorList.add(visitor);
                    }

                    hashVisitorList.add(visitorList);


                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();


            }
        }
    }



    public void signal() {

        ModuleMetricsHandler.getInstance().getLatch().countDown();
    }

    public List<List<ModuleMetricsVisitor>> getHashVisitorList() {
        return hashVisitorList;
    }

    public void setHashVisitorList(List<List<ModuleMetricsVisitor>> hashVisitorList) {
        this.hashVisitorList = hashVisitorList;
    }
}
















































































