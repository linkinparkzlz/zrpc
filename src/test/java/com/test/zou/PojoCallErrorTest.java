package com.test.zou;


import com.zou.exception.InvokeModuleException;
import com.zou.services.PersonManage;
import com.zou.services.pojo.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PojoCallErrorTest {


    public static void test1(PersonManage personManage) {

        try {
            personManage.check();
        } catch (InvokeModuleException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void test2(PersonManage personManage) {

        try {
            Person person = new Person();
            person.setId(20182822);
            person.setName("ZzzzL");
            person.setAge(10);
            personManage.checkAge(person);
        } catch (InvokeModuleException e) {

            System.out.println(e.getMessage());

        }
    }


    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext
                = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        PersonManage personManage = (PersonManage) classPathXmlApplicationContext.getBean("personManage");

        try {
            test1(personManage);
            test2(personManage);
        } finally {
            classPathXmlApplicationContext.destroy();
        }
    }


}













































