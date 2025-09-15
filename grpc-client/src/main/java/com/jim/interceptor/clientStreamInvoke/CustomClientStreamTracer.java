package com.jim.interceptor.clientStreamInvoke;


import io.grpc.ClientStreamTracer;
import io.grpc.Metadata;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author JimLam
 * @Date 2025/9/15 23:43
 * @Description 客户端流式调用追踪器，给工厂创建的追踪器
 */
@Slf4j
public class CustomClientStreamTracer<ReqT, RespT> extends ClientStreamTracer {
    //outbound 对于请求相关操作的拦截

    @Override
    //用于输出响应头
    public void outboundHeaders() {
        log.debug("client: 用于输出请求头.....");
        super.outboundHeaders();
    }

    @Override
    //设置消息编号
    public void outboundMessage(int seqNo) {
        log.debug("client: 设置流消息的编号 {} ", seqNo);
        super.outboundMessage(seqNo);
    }

    @Override
    public void outboundUncompressedSize(long bytes) {
        log.debug("client: 获得未压缩消息的大小 {} ", bytes);
        super.outboundUncompressedSize(bytes);
    }

    @Override
    //用于获得 输出消息的大小
    public void outboundWireSize(long bytes) {
        log.debug("client: 用于获得 输出消息的大小 {} ", bytes);
        super.outboundWireSize(bytes);
    }

    @Override
    //拦截消息发送
    public void outboundMessageSent(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
        log.debug("client: 监控请求操作 outboundMessageSent {} ", seqNo);
        super.outboundMessageSent(seqNo, optionalWireSize, optionalUncompressedSize);
    }


    //inbound  对于相应相关操作的拦截
    @Override
    public void inboundHeaders() {
        log.debug("用于获得响应头....");
        super.inboundHeaders();
    }

    @Override
    public void inboundMessage(int seqNo) {
        log.debug("获得响应消息的编号...{} ",seqNo);
        super.inboundMessage(seqNo);
    }

    @Override
    public void inboundWireSize(long bytes) {
        log.debug("获得响应消息的大小...{} ",bytes);
        super.inboundWireSize(bytes);
    }

    @Override
    public void inboundMessageRead(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
        log.debug("集中获得消息的编号 ，大小 ，未压缩大小 {} {} {}", seqNo, optionalWireSize, optionalUncompressedSize);
        super.inboundMessageRead(seqNo, optionalWireSize, optionalUncompressedSize);
    }

    @Override
    public void inboundUncompressedSize(long bytes) {
        log.debug("获得响应消息未压缩大小 {} ",bytes);
        super.inboundUncompressedSize(bytes);
    }

    @Override
    public void inboundTrailers(Metadata trailers) {
        log.debug("响应结束..");
        super.inboundTrailers(trailers);
    }
}
