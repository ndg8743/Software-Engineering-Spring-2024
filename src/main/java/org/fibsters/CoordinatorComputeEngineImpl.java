package org.fibsters;

import org.fibsters.interfaces.CoordinatorComputeEngine;
import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.interfaces.Result;

import java.util.Queue;

public class CoordinatorComputeEngineImpl implements CoordinatorComputeEngine {

    private FibCalcComputeEngineImpl fibCalcComputeEngine;
    private final FibSpiralComputeEngineImpl fibSpiralComputeEngine;
    private final DataStorageImpl dataStorage;
    private final ComputeJobPoolImpl jobPool;
    private Queue<InputPayload> inputPayloadQueue; // allowing for async input payloads for when jobPool is busy.

    private Queue<OutputPayload> outputPayloadQueue; // allowing for async output payloads for when jobPool is busy.

    public CoordinatorComputeEngineImpl(DataStorageImpl dataStorage) {
        this.dataStorage = dataStorage;
        // The rest is very tightly coupled to the implementation of the compute engines.
        // Not sure if this is the best way to do this.

        //this.fibCalcComputeEngine = new FibCalcComputeEngineImpl(outputPayload);
        this.fibSpiralComputeEngine = new FibSpiralComputeEngineImpl();
        this.jobPool = new ComputeJobPoolImpl();
        this.jobPool.start();
    }

    @Override // @Override is not strictly needed, but it is good practice to use it
    public Result<InputPayload> parseInputPayload(String inputPayloadString) {
        return dataStorage.parseInputPayload(inputPayloadString);
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
    public ComputeJob createComputeJobFromInputPayload(InputPayload inputPayload) {
        OutputPayloadImpl outputPayload = new OutputPayloadImpl(0, inputPayload, ComputeJobStatus.UNSTARTED);
        FibCalcComputeEngineImpl fibCalcCE = new FibCalcComputeEngineImpl(outputPayload);

        fibCalcCE.setInputPayload(inputPayload);

        return fibCalcCE;
    }

    @Override
    public void processCompletedJob(ComputeJob job) {

    }

    @Override
    public void queueJob(ComputeJob job) {
        jobPool.addJob(job);
    }

    @Override
    public ComputeJobStatus getJobStatus(ComputeJob job) {
        return job.getStatus();
    }

}
