package com.dhy.duck.protocol.dubbo;

public class DubboServer {

    public static void main(String[] args) {
        DubboServer httpServer = new DubboServer();
        httpServer.start("loalhost", 8080);
    }

    public void start(String hostname, Integer port) {


    }

    public void stop() {

    }
}
