package com.jim;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author JimLam
 * @Date 2025/9/14 15:52
 * @Description grpc双向流式调用
 */
@Slf4j
public class GrpcClient2 {
    public static void main(String[] args) {
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(localhost);
        StreamObserver<HelloRequest> helloRequestStreamObserver = helloServiceStub.sayHello2(new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                log.info("客户端的响应参数为:{}", helloResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("客户端接收完了服务端响应的数据!");
            }
        });

        for (int i = 0; i < 3; i++) {
            HelloRequest build = HelloRequest.newBuilder().setName("jim" + i).build();
            helloRequestStreamObserver.onNext(build);
        }

        helloRequestStreamObserver.onCompleted();
        try {
            localhost.awaitTermination(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
