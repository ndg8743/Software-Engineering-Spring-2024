package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;
import org.fibsters.InputPayloadImpl;

/**
 * Handles the request and returns the response.
 * Initalization, reading the request, and writing the response are all handled by the WebIO server.
 */
public interface CoordinatorComputeEngine {

    /* ---------------- Pass through methods. ---------------- */
    // TODO: Figure out if pass through methods are the best way to do this. ¯\_(ツ)_/¯

    /**
     * Passes the input payload to the data storage layer to be sanitized and validated.
     */
    Result<InputPayloadImpl> parseInputPayload(String inputPayloadString);

    Result<OutputPayload> parseOutputPayload(ComputeJob completedJob);

    /* ---------------- Process methods. ---------------- */

    /**
     * Checks for new jobs, processes them, and updates the job pool.
     * Checks for completed jobs, updates the job pool, and sends the results to the client.
     */
    void jobPoolLoop();

    /**
     * Processes the inputPayloadQueue, creates jobs, and adds them to the jobPool.
     */
    void payloadPoolLoop();

    /**
     * Creates FibCalcTasks, FibSpiralTasks, or other ComputeJobs from the inputPayload.
     */
    ComputeJob createComputeJobFromInputPayload(InputPayloadImpl inputPayload);

    /**
     * Calls parseOutputPayload in the dataStorage layer.
     * Removes the job from the jobPool.
     * Sends back OutputPayload to the client ( with whatever status it has ).
     */
    void processCompletedJob(ComputeJob job);
    
    void queueJob(ComputeJob job);

    ComputeJobStatus getJobStatus(ComputeJob job);

}




