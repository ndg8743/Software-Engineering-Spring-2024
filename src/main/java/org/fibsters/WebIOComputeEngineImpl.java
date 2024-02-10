package org.fibsters;

import com.sun.net.httpserver.HttpExchange;
import org.fibsters.interfaces.*;

import java.io.IOException;
import java.util.ArrayList;

public class WebIOComputeEngineImpl implements WebIOComputeEngine {
    FibCalcComputeEngineImpl fibCalcComputeEngine;
    FibSpiralComputeEngineImpl fibSpiralComputeEngine;
    DataStorageImpl dataStorage;
    ComputeJobPoolImpl jobPool;

    @Override // @Override is not strictly needed, but it is good practice to use it
    public Result<InputPayload> parseInputPayload(String inputPayloadString) {
        return null;
    }

    @Override
    public Result<OutputPayload> parseOutputPayload(ComputeJob completedJob) {
        return null;
    }

    @Override
    public void jobPoolLoop() {

    }

    @Override
    public void payloadPoolLoop() {

    }

    @Override
    public ArrayList<ComputeJob> createComputeJobsFromInputPayload(InputPayload inputPayload) {
        return null;
    }

    @Override
    public void processCompletedJob(ComputeJob job) {

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

    @Override
    public void handleRequest(HttpExchange exchange) throws IOException {

    }

    @Override
    public void handleResponse(HttpExchange exchange) throws IOException {

    }

    @Override
    public void handleGetRequest(HttpExchange exchange) throws IOException {

    }

    @Override
    public void handlePostRequest(HttpExchange exchange) throws IOException {

    }
    // What is the difference between implementing an interface and extending a class?


}
