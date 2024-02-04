package org.fibsters;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

/** Handles the request and returns the response.
 * Initalization, reading the request, and writing the response are all handled by the WebIO server.
 */
 public interface WebIOComputeEngine extends HttpHandler {
    /** Member variables
     * 1. jobPool: A pool of jobs to be processed. ComputeJobPool jobPool;
     * 2. dataStorage: A data storage layer to save and load payloads. DataStorage dataStorage;
     * 3. inputPayloadQueue: A queue of input payloads to be processed. Queue<inputPayload> inputPayloadQueue;
     */

    /* ---------------- Pass through methods. ---------------- */
    // TODO: Figure out if pass through methods are the best way to do this. ¯\_(ツ)_/¯
    /** Passes the input payload to the data storage layer to be sanitized and validated. */
    Result<InputPayload> parseInputPayload(String inputPayloadString);
    public Result<OutputPayload> parseOutputPayload(ComputeJob CompletedJob);

    /* ---------------- Process methods. ---------------- */
    /**
     * Checks for new jobs, processes them, and updates the job pool.
     * Checks for completed jobs, updates the job pool, and sends the results to the client.
     */
    void JobPoolLoop();

    /**
     * Processes the inputPayloadQueue, creates jobs, and adds them to the jobPool.
     */
    void PayloadPoolLoop();
    ArrayList<ComputeJob> createComputeJobsFromInputPayload(InputPayload inputPayload);

    /**
     * Calls parseOutputPayload in the dataStorage layer.
     * Removes the job from the jobPool.
     * Sends back OutputPayload to the client ( with whatever status it has ).
     */
    void processCompletedJob(ComputeJob job);

    /* ----------- HttpHandler Boilerplate methods. ----------- */
    public void handle(HttpExchange exchange) throws IOException;
    public void handleRequest(HttpExchange exchange) throws IOException;
    public void handleResponse(HttpExchange exchange) throws IOException;
    public void handleGetRequest(HttpExchange exchange) throws IOException;
    public void handlePostRequest(HttpExchange exchange) throws IOException;
}
interface ComputeJobPool {
    // i.e. public class MyComputeJobPool implements ComputeJobPool { ... } blah blah
    void addJob(ComputeJob job);
    void removeJob(ComputeJob job);
    void processJob(ComputeJob job);
    void processAllJobs();
    void getJobStatus(ComputeJob job);
    void getJobResults(ComputeJob job);
}
interface ComputeJob extends Runnable {
    void setInputPayload(InputPayload inputPayload);
    void getInputPayload();

}
interface ComputeJobStatus {
    public String getStatus();
    public String getErrorMessage();
    public String getErrorType();
}

// Generic wrapper interface for the result of a computation.
// There will be a SuccessResult and a FailureResult class that implement this interface.
// i.e. public class SuccessResult<T> implements Result<T> { ... }
// Then in processPayload, we can return a new SuccessResult or FailureResult.
interface Result<T> {
    boolean isSuccess();
    T getData();
    String getErrorMessage();
}




