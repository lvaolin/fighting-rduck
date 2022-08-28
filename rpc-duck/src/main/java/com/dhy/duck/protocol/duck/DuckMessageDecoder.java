package com.dhy.duck.protocol.duck;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Title 大黄鸭协议解码器
 * @Description
 * @Author lvaolin
 * @Date 2021/5/23 10:36
 **/
public class DuckMessageDecoder extends LengthFieldBasedFrameDecoder {
    MarshallingDecoder marshallingDecoder;
    public DuckMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
        marshallingDecoder = new MarshallingDecoder();

    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame==null) {
            return null;
        }
        DuckMessage message = new DuckMessage();
        Header header = new Header();
        header.setMagicNumber(frame.readInt());
        header.setLength(frame.readInt());
        header.setMsgId(frame.readLong());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        //附件的长度
        int size = frame.readInt();
        HashMap<String, Object> attachment = new HashMap<String,Object>();
        //读取附件
        for (int i = 0; i <size ; i++) {
            //key的长度
            int keySize = frame.readInt();
            byte[] keyBytes = new byte[keySize];
            frame.readBytes(keyBytes);
            String key = new String(keyBytes, "UTF-8");
            //@todo 对象反序列化
            Object value = marshallingDecoder.decode(frame);
            attachment.put(key,value);
        }
        //附件
        header.setAttachment(attachment);
        Object body = null;
        if (frame.readableBytes()>4) {
            //@todo 读取body对象，反序列化成对象
            body = marshallingDecoder.decode(frame);
        }
        message.setHeader(header);
        message.setBody(body);
        return  message;
    }
}
