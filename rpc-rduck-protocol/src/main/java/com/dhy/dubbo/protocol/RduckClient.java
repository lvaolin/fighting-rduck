package com.dhy.dubbo.protocol;

import com.dhy.dubbo.protocol.rduck.HeartBeatReqHandler;
import com.dhy.dubbo.protocol.rduck.LoginAuthReqHandler;
import com.dhy.dubbo.protocol.rduck.RduckMessageDecoder;
import com.dhy.dubbo.protocol.rduck.RduckMessageEncoder;
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
public class RduckClient {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    public static void main(String[] args) throws Exception{
        new RduckClient().connect("127.0.0.1",9999);
    }
    public void connect(String host,int port) throws Exception{
        try {
            Bootstrap bootstrap = new Bootstrap();
            Bootstrap channel = bootstrap.group(this.group).channel(NioSocketChannel.class);
            channel.option(ChannelOption.TCP_NODELAY,true);
            channel.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //解码器
                    pipeline.addLast(new RduckMessageDecoder(1024*1024,4,4));
                    //编码器
                    pipeline.addLast("MessageEncoder",new RduckMessageEncoder());
                    //超时处理
                    pipeline.addLast("ReadTimeoutHandler",new ReadTimeoutHandler(90));
                    //登录请求处理
                    pipeline.addLast("LoginAuthReqHandler",new LoginAuthReqHandler());
                    //心跳请求处理
                    pipeline.addLast("HeartBeatReqHandler",new HeartBeatReqHandler());
                }
            });
            ChannelFuture channelFuture = channel.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            scheduledExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        //重连
                        connect(host,port);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}

