package com.jim;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author JimLam
 * @Date 2025/9/14 17:45
 * @Description 客户端流式调用
 */
@Slf4j
public class GrpcClient3 {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(localhost);
        StreamObserver<HelloRequest> helloRequestStreamObserver = helloServiceStub.sayHello3(new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                log.info("客户端流式内容:{}", helloResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("客户端结束参数传递");
            }
        });

        for (int i = 0; i < 3; i++) {
            HelloRequest.Builder builder = HelloRequest.newBuilder();
            HelloRequest request = builder.setName("jim" + i).build();
            helloRequestStreamObserver.onNext(request);
            Thread.sleep(1000);
        }

        helloRequestStreamObserver.onCompleted();

        localhost.awaitTermination(4, TimeUnit.SECONDS);
    }
}
