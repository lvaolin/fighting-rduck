package com.dhy.server;

import com.dhy.duck.config.DuckConfig;
import com.dhy.duck.framework.LocalBeanFactory;
import com.dhy.duck.framework.URL;
import com.dhy.duck.protocol.rmi.RmiProtocol;
import com.dhy.server.impl.UserServiceImpl;
import com.dhy.server.itf.IUserServive;

import java.io.IOException;

/**
 * @Title Main   提供者启动类
 * @Description
 * @Author lvaolin
 * @Date 2021/3/12 22:17
 **/
public class Main {

    public static void main(String[] args) throws IOException {
        //注册中心配置信息
        DuckConfig duckConfig = DuckConfig.getInstance();
        duckConfig.setRegisterHost("127.0.0.1");
        duckConfig.setRegisterPort("2181");

        LocalBeanFactory.getInstance().addService(IUserServive.class.getName(),new UserServiceImpl());

        //向注册中心注册 服务名称以及服务器IP和PORT
        URL url = new URL("user-service","127.0.0.1",8089);
        //启动服务端 使用 rmi 协议
        RmiProtocol rmiProtocol = new RmiProtocol();
        rmiProtocol.start(url);
    }
}
