package com.dhy.dubbo.protocol.duck;

import com.dhy.dubbo.dto.RpcRequest;
import com.dhy.dubbo.framework.Protocol;
import com.dhy.dubbo.framework.URL;

/**
 * @Title DuckProtocol
 * @Description
 * @Author lvaolin
 * @Date 2021/7/11 15:40
 **/
public class DuckProtocol implements Protocol {
    @Override
    public Object send(URL url, RpcRequest rpcRequest) {
        return null;
    }

    @Override
    public void start(URL url) {

    }
}
