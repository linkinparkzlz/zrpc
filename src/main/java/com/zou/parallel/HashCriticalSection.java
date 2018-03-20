package com.zou.parallel;

import com.zou.config.SystemConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class HashCriticalSection {

    private static Integer partition = SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS;

    private final Map<Integer, Semaphore> integerSemaphoreMap = new ConcurrentHashMap<>();

    public final static long BASIC = 0xcbf29ce484222325L;

    public final static long PRIME = 0x100000001b3L;

    public HashCriticalSection() {

        boolean fair = SystemConfig.SYSTEM_PROPERTY_JMX_METRICS_LOCK_FAIR == 1;

        init(null, fair);

    }


    public static int hash(String key) {
        return Math.abs((int) (fnv1a64(key) % partition));
    }


    public static long fnv1a64(String key) {
        long hashCode = BASIC;

        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);

            if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch + 32);
            }

            hashCode ^= ch;
            hashCode *= PRIME;
        }

        return hashCode;
    }


    private void init(Integer counts, boolean fair) {

        if (counts != null) {
            partition = counts;
        }

        for (int i = 0; i < partition; i++) {
            integerSemaphoreMap.put(i, new Semaphore(1, fair));
        }
    }


    public void enter(int hashKey) {
        Semaphore semaphore = integerSemaphoreMap.get(hashKey);

        try {
            semaphore.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }


    public void exit(int hashKey) {

        Semaphore semaphore = integerSemaphoreMap.get(hashKey);
        semaphore.release();
    }


}


































































