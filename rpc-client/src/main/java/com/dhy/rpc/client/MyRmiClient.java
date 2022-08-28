package com.dhy.rpc.client;

import com.dhy.duck.framework.ProxyFactory;
import com.dhy.duck.framework.RpcContext;
import com.dhy.server.itf.IUserServive;
import com.dhy.server.dto.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MyRmiClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //通过jdk代理生成一个IUserServive代理对象
        IUserServive userServive= ProxyFactory.getProxy("user-service",IUserServive.class);
        System.out.println("获取代理对象成功：");

        try {
            while (true){
                System.out.println("调用方法before");
                userServive.getUserById(100L);

                System.out.println("异步请求开始");
                CompletableFuture completableFuture = RpcContext.get();
                User result = (User) completableFuture.get();
                System.out.println("响应回来了");
                System.out.println("调用方法after");
                System.out.println(result);
                //TimeUnit.SECONDS.sleep(1);
            }


        }catch (Exception e){
            e.printStackTrace();
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
