package com.jim.service;

import com.jim.HelloRequest;
import com.jim.HelloResponse;
import com.jim.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Jim_Lam
 * @description HelloGrpcImp
 */

@Slf4j
public class HelloGrpcImp extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        log.info("HelloGrpcImp.sayHello 被调用...");
        String name = request.getName();
        HelloResponse build = HelloResponse.newBuilder().setResult(name).build();
        log.info(String.valueOf(build));
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> sayHello2(StreamObserver<HelloResponse> responseObserver) {
        log.info("HelloGrpcImp.sayHello2 被调用...");
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest helloRequest) {
                log.info("服务端接收的昵称为:{}", helloRequest.getName());
                HelloResponse.Builder builder = HelloResponse.newBuilder();
                HelloResponse response = builder.setResult(helloRequest.getName()).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getCause().toString());
            }

            @Override
            public void onCompleted() {
                log.info("服务端接收完所有参数...");
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<HelloRequest> sayHello3(StreamObserver<HelloResponse> responseObserver) {
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest helloRequest) {
                log.info("接收到客户端的消息：{}", helloRequest.getName());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("客户端的所有消息都已经发送到服务端。。。");
                HelloResponse response = HelloResponse.newBuilder()
                        .setResult("处理完成! ")
                        .build();
               // 客户端流式 RPC 要求服务端必须发送一个响应消息
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void sayHello4(HelloRequest request, StreamObserver<HelloResponse> responseObserver){
        log.info("服务端接收的数据为:{}", request.getName());
        for (int i = 0; i < 5; i++) {
            HelloResponse response = HelloResponse.newBuilder().setResult("jimlam" + i).build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        responseObserver.onCompleted();
    }
}