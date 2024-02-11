package org.fibsters.interfaces;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface ComputeHttpHandler extends HttpHandler {
    void handle(HttpExchange exchange) throws IOException;
    void handleRequest(HttpExchange exchange) throws IOException;
    void handleResponse(HttpExchange exchange) throws IOException;
}
