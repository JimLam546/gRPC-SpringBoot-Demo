package com.jim.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Jim_Lam
 * @description: CustomClientInterceptor
 * @CreateTime: 2025-08-31  19:35
 * @Description: TODO
 */

@Slf4j
public class CustomClientSimpleInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        // 获取调用服务端方法的名称
        String fullMethodName = methodDescriptor.getFullMethodName();
        // fullMethodName: HelloService/SayHello
        log.info("fullMethodName: {}", fullMethodName);
        log.info("rpc 自定义简单拦截器逻辑被执行...");
        return channel.newCall(methodDescriptor, callOptions);
    }
}