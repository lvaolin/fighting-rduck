package com.dhy.duck.protocol.dubbo;

import com.dhy.duck.dto.RpcRequest;
import com.dhy.duck.framework.Protocol;
import com.dhy.duck.framework.URL;

public class DubboProtocol implements Protocol {
    @Override
    public Object send(URL url, RpcRequest rpcRequest) {
        return new DubboClient().send(url.getHost(), url.getPort(), rpcRequest);
    }

    @Override
    public void start(URL url) {
        DubboServer nettyServer = new DubboServer();
        nettyServer.start(url.getHost(),url.getPort());
    }
}
