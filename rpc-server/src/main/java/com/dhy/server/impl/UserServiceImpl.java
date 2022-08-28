package com.dhy.server.impl;

import com.dhy.duck.anntation.MyService;
import com.dhy.server.itf.IUserServive;
import com.dhy.server.dto.User;

@MyService
public class UserServiceImpl implements IUserServive {
    @Override
    public User getUserById(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setName("rpc-name");
        System.out.println("okl");
        return user;
    }
}
