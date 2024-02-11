package org.fibsters;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FibHttpHandler implements HttpHandler {
    private final CoordinatorComputeEngineImpl computeAPI;

    public FibHttpHandler(CoordinatorComputeEngineImpl api) {

        this.computeAPI = api;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String requestMethod = t.getRequestMethod();
        // Check if the request method is GET
        switch (requestMethod) {
            case "GET":
                handleGet(t);
                break;
            case "POST":
                handlePost(t);
                break;
            default:
                handleDefault(t);
                break;
        }
    }

    private void handleGet(HttpExchange t) throws IOException {
        String response = "get: hello world from fib handler";
        sendResponse(t, response);
    }
    /* curl.exe required ('curl' is an alias to some bs in powershell)
    Worst testing method ever. Need to do this first before Mockito since it's new to me.

    example invalid input:
    curl.exe -X POST http://127.0.0.1:8080/fib -d '{\"input\": \"hello\"}'
    example valid input:
    curl.exe -X POST -H "Content-Type: application/json" -d '{ \"UUID\": \"1234\", \"inputType\": \"csv\", \"delimiter\": \";\", \"outputType\": \"json\", \"outputSource\": \"output.json\" }' http://localhost:8080/fib
     */

    private void handlePost(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        String inputString = new String(is.readAllBytes());
        System.out.println(inputString);

        Result<InputPayload> result = computeAPI.parseInputPayload(inputString);
        if (result.isSuccess()) {
            String response = result.toJSON().toString();
            sendResponse(t, response);
            return;
        } else {
            String response = result.toJSON().toString();
            sendResponse(t, response);
            return;
        }
    }

    private void handleDefault(HttpExchange t) throws IOException {
        String response = "default: hello world from fib handler";
        sendResponse(t, response);
    }

    private void sendResponse(HttpExchange t, String response) throws IOException {
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
