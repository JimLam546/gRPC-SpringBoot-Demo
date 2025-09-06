package com.jim.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Jim_Lam
 * @description: CustomClientComplexInterceptor
 * @CreateTime: 2025-09-06  23:14
 * @Description: 自定义 Grpc 调用复杂拦截器
 */

@Slf4j
public class CustomClientComplexInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info("rpc 自定义复杂拦截器逻辑被执行...");
        return new CustomForwardingClientClass<>(channel.newCall(methodDescriptor, callOptions));
    }
}

/**
 * 自定义复杂拦截器
 * 内部原理是通过装饰器模式对简单拦截器进行了封装
 * @param <ReqT>
 * @param <RespT>
 */
@Slf4j
class CustomForwardingClientClass<ReqT, RespT> extends ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT> {
    protected CustomForwardingClientClass(ClientCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    protected void checkedStart(Listener<RespT> listener, Metadata metadata) throws Exception {
        log.info("发送请求数据前检查...");
        delegate().start(new CustomForwardingClientListener<>(listener), metadata);
    }

    @Override
    public void request(int numMessages) {
        //添加一些功能
        log.debug("request 方法被调用 ....");
        super.request(numMessages);
    }

    // todo 没有开启debug的打印，目前看不到下面的打印
    @Override
    //发送消息 缓冲区
    public void sendMessage(ReqT message) {
        log.debug("sendMessage 方法被调用... {} ", message);
        super.sendMessage(message);
    }

    @Override
    //开启半连接 请求消息无法发送，但是可以接受响应的消息
    public void halfClose() {
        log.debug("halfClose 方法被调用... 开启了半连接");
        super.halfClose();
    }
}

@Slf4j
class CustomForwardingClientListener<RespT> extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {
    protected CustomForwardingClientListener(ClientCall.Listener<RespT> delegate) {
        super(delegate);
    }

    /**
     * 每次进行一元调用时下面的监听器会被触发3次，因为以下3个钩子都会触发一次（触发的顺序下同）
     * 1. onHeaders(Metadata headers)：当接收到服务器响应的初始元数据（headers）时。
     * 2. onMessage(RespT message)：当接收到服务器的响应消息时。虽然 一元调用 通常只有一个消息，但某些实现或底层协议可能会分多次处理。
     * 3. onClose(Status status, Metadata trailers)
     * @return 无
     */
    @Override
    protected ClientCall.Listener<RespT> delegate() {
        log.info("拦截器中的监听器接收响应数据前检查...");
        return super.delegate();
    }

    @Override
    public void onHeaders(Metadata headers) {
        log.info("拦截器中的监听器拦截请求头信息...");
        super.onHeaders(headers);
    }

    @Override
    public void onMessage(RespT message) {
        log.info("拦截器中的监听器拦截服务端响应的数据信息...");
        super.onMessage(message);
    }


    @Override
    public void onClose(Status status, Metadata trailers) {
        log.info("拦截器中的监听器拦截，完成一次rpc调用时触发...");
        super.onClose(status, trailers);
    }
}