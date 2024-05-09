package org.fibsters;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {

    // TODO: Make a config file for the port number and other settings.
    public static void main(String[] args) {
        int httpPort = 8080; // default port.
        int grpcPort = 8999;

        if (args.length > 0) {
            httpPort = Integer.parseInt(args[0]);
        }

        FibHttpHandlerAPI handler = new FibHttpHandlerAPI(httpPort);

        System.out.println("[Fib] HTTP server started at " + httpPort);

        try {
            handler.start();
            handleGrpcServer(grpcPort, handler.getComputeApi());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleGrpcServer(int port, CoordinatorComputeEngineImpl api) {
        try {
            Server server = ServerBuilder.forPort(port).addService(new DataStorageImpl()).addService(new ComputeGrpcService(api)).build();

            server.start();
            System.out.println("[Fib] GRPC Server started at " + server.getPort());
            server.awaitTermination();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }
    }

}
