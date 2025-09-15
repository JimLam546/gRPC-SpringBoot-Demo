package com.jim;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author JimLam
 * @Date 2025/9/14 23:33
 * @Description 服务端流式返回
 */
@Slf4j
public class GrpcClient4 {
    public static void main(String[] args) {
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("127.0.0.1", 9090)
                .usePlaintext()
                .build();

        log.info("客户端启动成功!");
        HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(localhost);
        stub.sayHello4(HelloRequest.newBuilder().setName("Jim").build(), new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                log.info("客户端接收到的响应数据:{}", helloResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
                log.error(String.valueOf(throwable.getCause()));
            }

            @Override
            public void onCompleted() {
                log.info("服务端的数据传输完成!");
            }
        });

        try {
            localhost.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        localhost.shutdown();
    }
}
