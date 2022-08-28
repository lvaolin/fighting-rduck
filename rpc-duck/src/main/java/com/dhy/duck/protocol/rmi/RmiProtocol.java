package com.dhy.duck.protocol.rmi;

import com.dhy.duck.dto.RpcRequest;
import com.dhy.duck.framework.Protocol;
import com.dhy.duck.framework.URL;

public class RmiProtocol implements Protocol {
    @Override
    public Object send(URL url, RpcRequest rpcRequest) {
        return new RmiClient().send(url.getHost(), url.getPort(), rpcRequest);
    }

    @Override
    public void start(URL url) {
        try {
            //使用RMI协议
            RmiServer myServer = new RmiServer(url.getApplicationName());
            myServer.start(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
