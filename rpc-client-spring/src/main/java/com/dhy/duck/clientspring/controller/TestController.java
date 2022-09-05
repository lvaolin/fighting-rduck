package com.dhy.duck.clientspring.controller;

import com.dhy.duck.anntation.MyReference;
import com.dhy.duck.framework.RpcContext;
import com.dhy.server.dto.User;
import com.dhy.server.itf.IUserServive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Project rduck
 * @Description 主要用途描述
 * @Author lvaolin
 * @Date 2022/8/28 下午10:09
 */
@RestController
public class TestController {
    @Autowired
    private IUserServive userServive;

    @RequestMapping("/test")
    public Object test() throws ExecutionException, InterruptedException {

        System.out.println("666");
        //return "ok";
        userServive.getUserById(1L);
        System.out.println("异步请求开始");
        CompletableFuture completableFuture = RpcContext.get();
        User result = (User) completableFuture.get();
        System.out.println("响应回来了");
        System.out.println("调用方法after");
        System.out.println(result);
        return result;
    }
}
