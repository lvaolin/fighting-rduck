package com.dhy.duck.framework;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Project rduck
 * @Description 主要用途描述
 * @Author lvaolin
 * @Date 2022/9/5 上午11:39
 */
public class MyReferenceBean<T> implements FactoryBean<T> {

    Class<T> clazz;

    public MyReferenceBean(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    public T getObject() throws Exception {
        return ProxyFactory.getProxy("user-service",clazz);
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
