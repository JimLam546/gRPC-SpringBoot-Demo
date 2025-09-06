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


}