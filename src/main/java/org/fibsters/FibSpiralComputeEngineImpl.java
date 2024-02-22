package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.FibSpiralComputeEngine;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

public class FibSpiralComputeEngineImpl implements FibSpiralComputeEngine {

    private int chunk;

    @Override
    public void setInputPayload(InputPayload inputPayload) {

    }

    @Override
    public OutputPayload getOutputPayload() {
        return null;
    }

    @Override
    public ComputeJobStatus getStatus() {
        return null;
    }

    @Override
    public void setStatus(ComputeJobStatus status) {

    }

    @Override
    public int getTotalSize() {
        return 0;
    }

    @Override
    public void setStartIndex(int startIndex) {

    }

    @Override
    public void setEndIndex(int endIndex) {

    }

    @Override
    public InputPayload getInputPayload() {
        return null;
    }

    @Override
    public ComputeJob clone() {
        return null;
    }

    @Override
    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    @Override
    public void run() {

    }

}
