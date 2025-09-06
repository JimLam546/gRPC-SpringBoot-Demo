package com.jim;

import com.jim.service.HelloGrpcImp;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @author Jim_Lam
 * @description GrpcService
 */

public class GrpcService {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. 绑定端口
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9090);
        // 2. 发布服务
        serverBuilder.addService(new HelloGrpcImp());
        // 3. 创建服务对象
        Server server = serverBuilder.build();

        server.start();
        server.awaitTermination();
    }
}