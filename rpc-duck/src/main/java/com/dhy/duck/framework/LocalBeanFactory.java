package com.dhy.duck.framework;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务实现类 本地注册表
 */
public class LocalBeanFactory {
    static LocalBeanFactory myBeanFactory = new LocalBeanFactory();
    Map<String,Object> beanMap = new ConcurrentHashMap<>();
    private LocalBeanFactory(){
        //beanMap.put(IUserServive.class.getName(),new UserServiceImpl());
    }

    /**
     * 根据接口名称查询实现者
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        System.out.println(beanName);
        return beanMap.get(beanName);
    }

    /**
     * 单例模式
     * @return
     */
    public static LocalBeanFactory getInstance(){
        return myBeanFactory;
    }


    /**
     * 根据接口名称保存实现者
     * @param name
     * @param object
     */
    public void addService(String name,Object object){
        beanMap.put(name,object);
    }
}
