package com.dhy.duck.framework;

import java.util.concurrent.CompletableFuture;

/**
 * @Title RpcContext
 * @Description
 * @Author lvaolin
 * @Date 2021/7/11 18:58
 **/
public class RpcContext {
    private static ThreadLocal<CompletableFuture> result = new ThreadLocal<CompletableFuture>();
    public static void set(CompletableFuture completableFuture){
        result.set(completableFuture);
    }
    public static CompletableFuture get(){
        return result.get();
    }
}
