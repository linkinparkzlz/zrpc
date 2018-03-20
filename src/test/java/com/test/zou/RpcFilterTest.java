package com.test.zou;

import com.zou.exception.RejectResponseException;
import com.zou.services.Cache;
import com.zou.services.Store;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcFilterTest {


    public static void main(String[] args) {


        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        Cache cache = (Cache) classPathXmlApplicationContext.getBean("cache");

        for (int i = 0; i < 100; i++) {

            String s = String.valueOf(i);

            try {
                cache.put(s, s);
            } catch (RejectResponseException e) {
                System.out.println(e.getMessage());

            }
        }


        for (int i = 0; i < 100; i++) {

            String s = String.valueOf(i);

            try {
                System.out.println((String) cache.get(s));
            } catch (RejectResponseException e) {
                System.out.println(e.getMessage());

            }
        }


        Store store = (Store) classPathXmlApplicationContext.getBean("store");

        for (int i = 0; i < 100; i++) {

            String s = String.valueOf(i);

            try {
                store.save(s);
                store.save(i);
            } catch (RejectResponseException e) {
                System.out.println(e.getMessage());
            }
        }

        classPathXmlApplicationContext.destroy();


    }


}































































