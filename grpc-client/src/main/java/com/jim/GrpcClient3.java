package com.jim;


import com.jim.interceptor.clientStreamInvoke.CustomClientStreamInterceptor;
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
        // todo 学习客户端流式调用，为什么使用工厂模式生成追踪器，原理是什么?
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .intercept(new CustomClientStreamInterceptor())
                .build();

        HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(localhost);
        // 服务端多个响应数据时，创建响应请求泛型的的匿名内部类
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

        // 上面创建响应匿名内部类，这里给服务端传输数据
        for (int i = 0; i < 3; i++) {
            HelloRequest.Builder builder = HelloRequest.newBuilder();
            HelloRequest request = builder.setName("jim" + i).build();
            helloRequestStreamObserver.onNext(request);
            Thread.sleep(1000);
        }

        helloRequestStreamObserver.onCompleted();

        // 客户端处保持建立的连接时间设置
        // 必须设置，不然直接结束了
        localhost.awaitTermination(4, TimeUnit.SECONDS);
    }
}
