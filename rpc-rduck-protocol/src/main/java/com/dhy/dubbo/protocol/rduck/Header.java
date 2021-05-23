package com.dhy.dubbo.protocol.rduck;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title 大黄鸭协议 消息头
 * @Description
 * @Author lvaolin
 * @Date 2021/5/23 10:01
 **/
public class Header {
    /**
     * 魔数  用来识别大黄鸭协议 99dc 固定，01主版本可变，01次版本可变
     */
    private int magicNumber=0x99dc0101;
    /**
     * 消息总长度（header+body）
     */
    private int length;

    /**
     * 消息 ID, 定位响应与请求的关系
     */
    private long msgId;

    /**
     * 会话 ID,定位消息与通道的关系
     */
    private long sessionId;
    /**
     * 消息的类别：
     * 0 请求，1响应，2不需要响应的请求消息
     * 3、握手请求，4 握手响应
     * 5、心跳请求  6 心跳响应
     */
    private byte type;
    /**
     * 消息优先级
     */
    private byte priority;
    /**
     * 附件信息，供传递额外参数使用
     */
    private Map<String,Object> attachment = new HashMap<String,Object>();

    public int getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }



    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Header{" +
                "magicNumber=" + magicNumber +
                ", length=" + length +
                ", msgId=" + msgId +
                ", sessionId=" + sessionId +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
