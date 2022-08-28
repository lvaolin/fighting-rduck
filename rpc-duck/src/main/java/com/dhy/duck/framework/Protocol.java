package com.dhy.duck.framework;

import com.dhy.duck.dto.RpcRequest;

/**
 * 通信协议抽象
 */
public interface Protocol {

    /**
     * 客户端发送请求获取响应（客户端使用）
     * @param url
     * @param rpcRequest
     * @return
     */
    Object send(URL url, RpcRequest rpcRequest);

    /**
     * 启动服务端监听（服务端使用）
     * 同时将服务地址注册到注册中心
     * @param url
     */
    void start(URL url);

}
