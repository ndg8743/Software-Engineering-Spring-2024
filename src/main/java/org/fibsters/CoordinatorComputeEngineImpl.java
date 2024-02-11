package org.fibsters;

import com.sun.net.httpserver.HttpExchange;
import org.fibsters.interfaces.CoordinatorComputeEngine;
import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.interfaces.Result;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

public class CoordinatorComputeEngineImpl implements CoordinatorComputeEngine {
    private FibCalcComputeEngineImpl fibCalcComputeEngine;
    private FibSpiralComputeEngineImpl fibSpiralComputeEngine;
    private DataStorageImpl dataStorage;
    private ComputeJobPoolImpl jobPool;
    private Queue<InputPayload> inputPayloadQueue; // allowing for async input payloads for when jobPool is busy.

    public CoordinatorComputeEngineImpl(DataStorageImpl dataStorage) {
        this.dataStorage = dataStorage;
        // The rest is very tightly coupled to the implementation of the compute engines.
        // Not sure if this is the best way to do this.
        this.fibCalcComputeEngine = new FibCalcComputeEngineImpl();
        this.fibSpiralComputeEngine = new FibSpiralComputeEngineImpl();
        this.jobPool = new ComputeJobPoolImpl();
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
    public ArrayList<ComputeJob> createComputeJobsFromInputPayload(InputPayload inputPayload) {
        return null;
    }

    @Override
    public void processCompletedJob(ComputeJob job) {

    }
}
