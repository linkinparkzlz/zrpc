package com.test.zou;

import com.zou.exception.InvokeTimeoutException;
import com.zou.services.PersonManage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PojoTimeoutCallTest {

    public static void main(String[] args) {


        ClassPathXmlApplicationContext classPathXmlApplicationContext
                = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        PersonManage personManage = (PersonManage) classPathXmlApplicationContext.getBean("personManage");

        try {
            long timeout = 32L;
            personManage.query(timeout);
        } catch (InvokeTimeoutException e) {
            System.out.println(e.getMessage());

        } finally {
            classPathXmlApplicationContext.destroy();
        }


    }


}


































