package com.test.zou;


import com.zou.services.PersonManage;
import com.zou.services.pojo.Person;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
public class PojoCallTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        PersonManage manage = (PersonManage) classPathXmlApplicationContext.getBean("personManage");

        Person person = new Person();

        person.setId(20180311);
        person.setName("zoulvzhou");
        person.setAge(10);


        int result = manage.save(person);

        System.out.println("call  pojo  rpc result : " + result);

        classPathXmlApplicationContext.destroy();


    }
}





































































