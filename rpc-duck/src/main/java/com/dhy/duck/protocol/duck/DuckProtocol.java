package com.dhy.duck.protocol.duck;

import com.dhy.duck.dto.RpcRequest;
import com.dhy.duck.framework.Protocol;
import com.dhy.duck.framework.URL;

/**
 * @Title DuckProtocol
 * @Description
 * @Author lvaolin
 * @Date 2021/7/11 15:40
 **/
public class DuckProtocol implements Protocol {
    @Override
    public Object send(URL url, RpcRequest rpcRequest) {
        try {
            DuckClient duckClient = new DuckClient();
            return duckClient.send("127.0.0.1",9999,rpcRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start(URL url) {
        try {
            new DuckServer().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
