package com.dhy.duck.protocol.rmi;


import com.dhy.duck.config.DuckConfig;
import com.dhy.duck.framework.URL;
import com.dhy.duck.register.zookeeper.zkutil.MyZkClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动socket监听
 * 接受请求数据
 * 响应数据
 */
public class RmiServer {

    static Executor executor = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(2));

    private  String applicationName = "";
    public RmiServer(String applicationName) throws Exception{
        if (applicationName==null) {
            throw new Exception("服务名称不能为空");
        }
        this.applicationName = applicationName;
    }

    public  void start(URL url) throws IOException {

        ServerSocket serverSocket = new ServerSocket(url.getPort());
        MyZkClient myZkClient = new MyZkClient(DuckConfig.getInstance());
        String rootName="/"+applicationName;
        //服务节点是否存在
        if (!myZkClient.exist(rootName)) {
            //创建
            myZkClient.createNode(rootName,"");
        }
        if (!myZkClient.exist(rootName+"/"+url.getHost()+":"+url.getPort())) {
            myZkClient.createEphemeralNode(rootName+"/"+url.getHost()+":"+url.getPort(),"");
        }
        //追加本服务节点的地址信息 到下级临时节点
        System.out.println("向zookeeper注册服务提供者地址："+url.getHost()+":"+url.getPort());
        while (true) {
            System.out.println("监听中----");
            Socket socket = serverSocket.accept();
            executor.execute(new RmiTask(socket));
        }


    }
}
