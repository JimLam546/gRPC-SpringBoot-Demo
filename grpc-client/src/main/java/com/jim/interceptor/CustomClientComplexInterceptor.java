package com.jim.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Jim_Lam
 * @description: CustomClientComplexInterceptor
 * @CreateTime: 2025-09-06  23:14
 * @Description: TODO
 */

@Slf4j
public class CustomClientComplexInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info("rpc 自定义复杂拦截器逻辑被执行...");
        return new CustomForwardingClientClass<>(channel.newCall(methodDescriptor, callOptions));
    }
}

@Slf4j
class CustomForwardingClientClass<ReqT, RespT> extends ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT> {
    protected CustomForwardingClientClass(ClientCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    protected void checkedStart(Listener<RespT> listener, Metadata metadata) throws Exception {
        log.info("发送请求数据前检查...");
        delegate().start(listener, metadata);
    }
}