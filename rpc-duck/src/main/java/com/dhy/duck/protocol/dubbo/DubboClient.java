package com.dhy.duck.protocol.dubbo;

import com.dhy.duck.dto.RpcRequest;

public class DubboClient {

    public String send(String hostname, Integer port, RpcRequest rpcRequest){
        System.out.println("发送成功");
        return "ok";
    }
}
