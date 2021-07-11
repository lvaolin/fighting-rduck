package com.dhy.dubbo.protocol.duck;

/**
 * @Title 大黄鸭协议 消息结构体
 * @Description
 * @Author lvaolin
 * @Date 2021/5/23 9:56
 **/
public class DuckMessage {
    /**
     * 消息头
     */
    private Header header;
    /**
     * 消息体: 对于请求消息里面存放方法的参数，对于响应消息里面存放方法的返回值
     */
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RduckMessage{" +
                "header=" + header.toString() +
                ", body=" + body +
                '}';
    }
}
