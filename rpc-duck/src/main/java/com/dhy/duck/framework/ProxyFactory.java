package com.dhy.duck.framework;

import java.lang.reflect.Proxy;

/**
 * 代理工厂，生成代理对象
 */
public class ProxyFactory {

    public static  <T> T getProxy(String applicationName,Class<T> interfaceClass){
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader()
                , new Class[]{interfaceClass}
                ,new MyInvocationHandler(applicationName)
        );
    }
}
