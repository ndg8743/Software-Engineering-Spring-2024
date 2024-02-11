package org.fibsters;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

// TODO: migrate to Spring Boot or another framework.
public class FibHttpHandlerAPI {
    private CoordinatorComputeEngineImpl api;
    private Integer port;
    private HttpServer server; // abstract away the implementation of the server.

    private FibHttpHandler fibHttpHandler;

    public FibHttpHandlerAPI(Integer givenPort) {
        port = givenPort;
    }

    /**
     * For testing purposes only.
     * @param fibHttpHandler
     */
    public void _setFibHttpHandler(FibHttpHandler fibHttpHandler) {
        this.fibHttpHandler = fibHttpHandler;
    }

    // start
    public void start() throws IOException {
        this.api = new CoordinatorComputeEngineImpl(new DataStorageImpl());

        server = HttpServer.create();
        server.bind(new InetSocketAddress(port), 0);
        if(fibHttpHandler == null) {
            fibHttpHandler = new FibHttpHandler(api);
        }
        server.createContext("/fib", (HttpHandler) fibHttpHandler);
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started at http://127.0.0.1:" + port + "/fib");
    }

}
