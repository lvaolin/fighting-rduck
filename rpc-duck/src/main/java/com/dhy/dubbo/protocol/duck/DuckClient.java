package com.dhy.dubbo.protocol.duck;

import com.dhy.dubbo.dto.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Title RduckClient
 * @Description
 * @Author lvaolin
 * @Date 2021/5/23 15:15
 **/
public class DuckClient {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    public Object send(String host, int port, RpcRequest rpcRequest) throws Exception{
        try {
            Bootstrap bootstrap = new Bootstrap();
            Bootstrap channel = bootstrap.group(this.group).channel(NioSocketChannel.class);
            channel.option(ChannelOption.TCP_NODELAY,true);
            channel.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //解码器
                    pipeline.addLast(new DuckMessageDecoder(1024*1024,4,4));
                    //编码器
                    pipeline.addLast("MessageEncoder",new DuckMessageEncoder());
                    //超时处理
                    pipeline.addLast("ReadTimeoutHandler",new ReadTimeoutHandler(90));
                    //登录请求处理
                    pipeline.addLast("LoginAuthReqHandler",new LoginAuthReqHandler());
                    //心跳请求处理
                    pipeline.addLast("HeartBeatReqHandler",new HeartBeatReqHandler());
                }
            });
            //建立通道连接
            ChannelFuture channelFuture = channel.connect(host, port).sync();
            //构造 duck协议报文
            DuckMessage duckMessage = new DuckMessage();
            duckMessage.setHeader(new Header());//报文头
            duckMessage.setBody(rpcRequest);//请求业务数据
            //发送请求
            channelFuture.channel().writeAndFlush(duckMessage);
            //接收响应
            Object result = new Object();
            channelFuture.channel().read().write(result);
            //channelFuture.channel().closeFuture().sync();
            return result;
        }finally {
//            scheduledExecutorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        TimeUnit.SECONDS.sleep(5);
//                        //重连
//                        connect(host,port);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            });
        }


    }
}

