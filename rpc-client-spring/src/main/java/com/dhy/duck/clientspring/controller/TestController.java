package com.dhy.duck.clientspring.controller;

import com.dhy.duck.anntation.MyReference;
import com.dhy.server.itf.IUserServive;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project rduck
 * @Description 主要用途描述
 * @Author lvaolin
 * @Date 2022/8/28 下午10:09
 */
@RestController
public class TestController {
    @MyReference
    private IUserServive userServive;

    @RequestMapping("/test")
    public Object test(){
        System.out.println("666");
        return "ok";
    }
}
