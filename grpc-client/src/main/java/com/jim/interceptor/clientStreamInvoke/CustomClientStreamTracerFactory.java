package com.jim.interceptor.clientStreamInvoke;


import io.grpc.ClientStreamTracer;
import io.grpc.Metadata;

/**
 * @Author JimLam
 * @Date 2025/9/15 23:42
 * @Description　客户端流式调用拦截器追踪器工厂
 */
public class CustomClientStreamTracerFactory<ReqT, RespT> extends ClientStreamTracer.Factory {
    @Override
    public ClientStreamTracer newClientStreamTracer(ClientStreamTracer.StreamInfo info, Metadata headers) {
        return new CustomClientStreamTracer<ReqT, RespT>();
    }
}
