package org.fibsters;

import org.fibsters.ComputeInputServiceGrpc.ComputeInputServiceImplBase;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FibHttpHandler extends ComputeInputServiceImplBase implements HttpHandler {

    private final CoordinatorComputeEngineImpl computeAPI;

    public FibHttpHandler(CoordinatorComputeEngineImpl api) {
        this.computeAPI = api;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        // Check if the request method is GET
        switch (requestMethod) {
            case "GET":
                handleGet(httpExchange);
                break;
            case "POST":
                handlePost(httpExchange);
                break;
            default:
                handleDefault(httpExchange);
                break;
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        String response = "get: hello world from fib handler";
        //String exampleJson = "{'calcFibNumbersUpTo': [1, 10, 25]}";

        //computeAPI.parseInputPayload(exampleJson);

        sendResponse(httpExchange, response);
    }

    /* curl.exe required ('curl' is an alias to some bs in powershell)
    Worst testing method ever. Need to do this first before Mockito since it's new to me.

    example invalid input:
    curl.exe -X POST http://127.0.0.1:8080/fib -d '{\"input\": \"hello\"}'
    example valid input:
    curl.exe -X POST -H "Content-Type: application/json" -d '{ \"uniqueID\": \"1234\", \"inputType\": \"csv\", \"delimiter\": \";\", \"outputType\": \"json\", \"outputSource\": \"output.json\" }' http://localhost:8080/fib
    example valid input: 'calcFibNumbersUpTo': [1, 10, 25]}
    curl.exe -X POST -H "Content-Type: application/json" -d '{ \"uniqueID\": \"1234\", \"inputType\": \"json\", \"delimiter\": \",\", \"outputType\": \"json\", \"outputSource\": \"output.json\", \"payloadData\": { \"calcFibNumbersUpTo\": [1, 10, 25] } }' http://localhost:8080/fib
     */
    private void handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();

        String inputString = new String(inputStream.readAllBytes());

        System.out.println(inputString);

        String response = computeAPI.processInputStringForOutput(inputString);
        System.out.println("response" + response);

        /*

        Result<InputPayload> result = computeAPI.parseInputPayload(inputString);

        // TODO: handle the result
        ComputeJob job = computeAPI.createComputeJobFromInputPayload(result.getData());
        computeAPI.queueJob(job);

        OutputPayload output = job.getOutputPayload();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter())
                .create();
        String response = "";
        try {
            response = gson.toJson(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        sendResponse(httpExchange, response);
    }

    private void handleDefault(HttpExchange httpExchange) throws IOException {
        String response = "default: hello world from fib handler";

        sendResponse(httpExchange, response);
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());

        OutputStream outputStream = httpExchange.getResponseBody();

        outputStream.write(response.getBytes());
        outputStream.close();
    }

}
