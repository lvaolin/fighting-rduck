package com.dhy.server.itf;

import com.dhy.duck.anntation.MyReference;
import com.dhy.server.dto.User;

/**
 * @Project rduck
 * @Description 主要用途描述
 * @Author lvaolin
 * @Date 2022/9/5 下午4:14
 */
@MyReference
public class User1 implements IUserServive{
    @Override
    public User getUserById(Long userId) {
        return null;
    }
}
