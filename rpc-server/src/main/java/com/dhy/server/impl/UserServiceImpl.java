package com.dhy.server.impl;

import com.dhy.dubbo.anntation.MyService;
import com.dhy.server.itf.IUserServive;
import com.dhy.server.dto.User;

import java.util.concurrent.TimeUnit;

@MyService
public class UserServiceImpl implements IUserServive {
    @Override
    public User getUserById(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setName("rpc-name");
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("okl");
        return user;
    }
}
