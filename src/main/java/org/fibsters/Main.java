package org.fibsters;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {

    // TODO: Make a config file for the port number and other settings.
    public static void main(String[] args) {
        int port = 8080; // default port.

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        FibHttpHandlerAPI handler = new FibHttpHandlerAPI(port);

        try {
            handler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleGrpcServer(int port) {
        try {
            Server server = ServerBuilder.forPort(port).addService(new DataStorageImpl()).build();

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
