package com.dhy.duck.protocol.duck;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @Title RduckServer
 * @Description
 * @Author lvaolin
 * @Date 2021/5/23 15:15
 **/
public class DuckServer {

    public void bind() throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(3);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG,100);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new DuckMessageDecoder(1024*1024,4,4));
                p.addLast(new DuckMessageEncoder());
                p.addLast("readTimeoutHandler",new ReadTimeoutHandler(90));
                p.addLast(new LoginAuthRespHandler());
                p.addLast("heartBeatRespHandler",new HeartBeatRespHandler());
            }
        })  ;
        int port = 9999;
        serverBootstrap.bind("127.0.0.1",port);
        System.out.println("Rduck is ready ------"+port+"-----");
    }
}
