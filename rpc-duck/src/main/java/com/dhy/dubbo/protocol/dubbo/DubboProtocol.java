package com.dhy.dubbo.protocol.dubbo;

import com.dhy.dubbo.dto.RpcRequest;
import com.dhy.dubbo.framework.Protocol;
import com.dhy.dubbo.framework.URL;

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
