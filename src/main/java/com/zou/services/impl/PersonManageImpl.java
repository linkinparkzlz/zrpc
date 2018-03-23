package com.zou.services.impl;

import com.zou.services.PersonManage;
import com.zou.services.pojo.Person;

import java.util.concurrent.TimeUnit;

public class PersonManageImpl implements PersonManage {

    @Override
    public int save(Person p) {

        System.out.println("person data[" + p + "] has save");
        return 0;

    }

    @Override
    public void query(Person p) {


        try {

            TimeUnit.SECONDS.sleep(3);
            System.out.println("person data[" + p + "] has query");
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void query(long timeout) {

        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {

            e.printStackTrace();

        }
    }

    @Override
    public void check() {

        throw new RuntimeException("person check fail");

    }

    @Override
    public boolean checkAge(Person p) {

        if (p.getAge() < 18) {
            throw new RuntimeException("person chech age fail");
        } else {
            System.out.println("peron check age success");

            return true;
        }

    }
}





























































