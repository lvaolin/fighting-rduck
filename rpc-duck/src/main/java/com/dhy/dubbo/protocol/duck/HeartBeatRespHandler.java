package com.dhy.dubbo.protocol.duck;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Title HeartBeatReqHandler
 * @Description  心跳响应处理
 * @Author lvaolin
 * @Date 2021/5/23 15:11
 **/
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        DuckMessage message = (DuckMessage) msg;
        // 返回心跳应答消息
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_REQ
                .value()) {
            System.out.println("Receive client heart beat message : ---> "
                    + message);
            DuckMessage heartBeat = buildHeatBeat();
            System.out
                    .println("Send heart beat response message to client : ---> "
                            + heartBeat);
            ctx.writeAndFlush(heartBeat);
        } else{
            ctx.fireChannelRead(msg);
        }

    }

    private DuckMessage buildHeatBeat() {
        DuckMessage message = new DuckMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }

}