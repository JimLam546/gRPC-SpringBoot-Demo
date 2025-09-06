package com.jim;

import com.jim.interceptor.CustomClientComplexInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jim_Lam
 * @description GrpcClenti
 */

@Slf4j
public class GrpcClient {
    public static void main(String[] args) {
        // 1. 创建通信的管道
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .intercept(new CustomClientComplexInterceptor())
                .build();
        // 2. 获取代理对象
        HelloServiceGrpc.HelloServiceBlockingStub helloService = HelloServiceGrpc.newBlockingStub(localhost);
        // 3. 完成 RPC 调用
        // 3.1 构建请求参数
        HelloRequest request = HelloRequest.newBuilder().setName("Jim_Lam").build();
        // 每次进行一次RPC调用，都会调用拦截器
        HelloResponse helloResponse = helloService.sayHello(request);
        log.info(helloResponse.getResult());
    }
}