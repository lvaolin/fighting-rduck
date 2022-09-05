package com.dhy.server.itf;

import com.dhy.duck.anntation.MyReference;
import com.dhy.server.dto.User;

@MyReference
public interface IUserServive {

    User getUserById(Long userId);
}
