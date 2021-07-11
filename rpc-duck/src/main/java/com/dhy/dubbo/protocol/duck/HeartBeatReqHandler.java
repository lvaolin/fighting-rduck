package com.dhy.dubbo.protocol.duck;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @Title HeartBeatReqHandler
 * @Description 心跳请求处理
 * @Author lvaolin
 * @Date 2021/5/23 15:11
 **/
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        DuckMessage message = (DuckMessage) msg;
        // 握手成功，主动发送心跳消息
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP
                .value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new HeartBeatTask(ctx), 0, 5000,
                    TimeUnit.MILLISECONDS);
        } else if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_RESP
                .value()) {
            System.out
                    .println("Client receive server heart beat message : ---> "
                            + message);
        } else
            ctx.fireChannelRead(msg);
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            DuckMessage heatBeat = buildHeatBeat();
            System.out
                    .println("Client send heart beat messsage to server : ---> "
                            + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private DuckMessage buildHeatBeat() {
            DuckMessage message = new DuckMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
