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
    public Integer getTotalSize() {
        return null;
    }

    @Override
    public void setStartIndex(Integer startIndex) {

    }

    @Override
    public void setEndIndex(Integer endIndex) {

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
    public void setChunk(int j) {
        this.chunk = j;
    }

    @Override
    public void run() {

    }
}
