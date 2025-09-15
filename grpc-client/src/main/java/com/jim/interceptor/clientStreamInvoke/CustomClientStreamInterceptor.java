package com.jim.interceptor.clientStreamInvoke;


import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author JimLam
 * @Date 2025/9/15 23:17
 * @Description 客户端流式调用拦截器
 */
@Slf4j
public class CustomClientStreamInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info("自定义客户端流式调用拦截器被调用-拦截请求...");
        // 客户端流式调用需要添加拦截器工厂
        callOptions = callOptions.withStreamTracerFactory(new CustomClientStreamTracerFactory<ReqT, RespT>());
        return channel.newCall(methodDescriptor, callOptions);
    }
}
