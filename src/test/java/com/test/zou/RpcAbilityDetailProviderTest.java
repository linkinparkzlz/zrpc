package com.test.zou;

import com.zou.core.AbilityDetail;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcAbilityDetailProviderTest {


    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        AbilityDetail abilityDetail = (AbilityDetail) classPathXmlApplicationContext.getBean("ability");

        StringBuilder stringBuilder = abilityDetail.listAbilityDetail(false);

        System.out.println(stringBuilder);

        classPathXmlApplicationContext.destroy();
    }


}
