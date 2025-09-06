package com.jim.service;

import com.jim.HelloRequest;
import com.jim.HelloResponse;
import com.jim.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;


/**
 * @author Jim_Lam
 * @description HelloGrpcImp
 */

public class HelloGrpcImp extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("HelloGrpcImp.sayHello 被调用...");
        String name = request.getName();
        HelloResponse build = HelloResponse.newBuilder().setResult(name).build();
        System.out.println(build);
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }


}