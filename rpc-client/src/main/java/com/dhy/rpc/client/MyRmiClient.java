package com.dhy.rpc.client;

import com.dhy.dubbo.framework.ProxyFactory;
import com.dhy.dubbo.framework.RpcContext;
import com.dhy.server.itf.IUserServive;
import com.dhy.server.dto.User;
import org.checkerframework.checker.guieffect.qual.UI;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MyRmiClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //通过jdk代理生成一个IUserServive代理对象
        ProxyFactory<IUserServive> proxyFactory = new ProxyFactory<IUserServive>();
        IUserServive userServive= proxyFactory.getProxy("user-service",IUserServive.class);
        System.out.println("获取代理对象成功：");

        try {
            System.out.println("调用方法before");
            User user = userServive.getUserById(100L);

            System.out.println("异步请求开始");
            CompletableFuture completableFuture = RpcContext.get();
            User result = (User) completableFuture.get();
            System.out.println("响应回来了");


            System.out.println("调用方法after");
            System.out.println(result);
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e){
            TimeUnit.SECONDS.sleep(3);
        }

//        synchronized (MyRmiClient.class){
//            try {
//                MyRmiClient.class.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }
}
