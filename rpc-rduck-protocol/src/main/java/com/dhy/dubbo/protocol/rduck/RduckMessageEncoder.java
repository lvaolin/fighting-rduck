package com.dhy.dubbo.protocol.rduck;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Title 大黄鸭协议编码器
 * @Description
 * @Author lvaolin
 * @Date 2021/5/23 10:36
 **/
public class RduckMessageEncoder extends MessageToMessageEncoder<RduckMessage> {

    MarshallingEncoder encoder;
    public RduckMessageEncoder() throws IOException {
        this.encoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RduckMessage msg, List<Object> list) throws Exception {
        if (msg==null||msg.getHeader()==null) {
            throw  new Exception("msg is null");
        }

        //来个缓冲区存放消息
        ByteBuf sendBuffer = Unpooled.buffer();
        //协议魔数
        sendBuffer.writeInt(msg.getHeader().getMagicNumber());
        //消息总长度
        sendBuffer.writeInt(msg.getHeader().getLength());
        //消息ID
        sendBuffer.writeLong(msg.getHeader().getMsgId());
        //信道会话ID
        sendBuffer.writeLong(msg.getHeader().getSessionId());
        //消息类别
        sendBuffer.writeByte(msg.getHeader().getType());
        //优先级
        sendBuffer.writeByte(msg.getHeader().getPriority());
        //附件的数量（必须的，不然解码时不知道读取多长）
        sendBuffer.writeInt(msg.getHeader().getAttachment().size());
        //将附件信息序列化
        msg.getHeader().getAttachment().forEach(
            (key,value)->{
                try {
                    byte[] keyBytes = key.getBytes("UTF-8");
                    //先写key的长度
                    sendBuffer.writeInt(keyBytes.length);
                    //再写key的内容
                    sendBuffer.writeBytes(keyBytes);
                    //@todo 关键所在：对象的序列化
                    encoder.encode(value,sendBuffer);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        );

        //消息体的序列化和写入缓冲区
        if (msg.getBody()!=null) {
            encoder.encode(msg.getBody(),sendBuffer);
        }else{
            sendBuffer.writeInt(0);
        }
        //消息的总长度
        sendBuffer.setInt(4,sendBuffer.readableBytes());
    }
}
