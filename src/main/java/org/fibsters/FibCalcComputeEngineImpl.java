package org.fibsters;

import org.fibsters.ComputeJobStatus;
import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.FibCalcComputeEngine;
import org.fibsters.interfaces.InputPayload;

public class FibCalcComputeEngineImpl implements FibCalcComputeEngine {
    ComputeJobStatus status;
    Integer startIndex;
    Integer endIndex;
    InputPayload inputPayload; // used for reference potentially(has uuid)

    public FibCalcComputeEngineImpl() {
        status = ComputeJobStatus.UNSTARTED;
        startIndex = 0;
        endIndex = 0;
    }

    @Override
    public void setInputPayload(InputPayload inputPayload) {
        this.inputPayload = inputPayload;
    }

    @Override
    public ComputeJobStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(ComputeJobStatus status) {
        this.status = status;
    }

    @Override
    public Integer getTotalSize() {
        return inputPayload.getTotalSize();
    }

    @Override
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public InputPayload getInputPayload() {

        return this.inputPayload;
    }

    @Override
    public void run() {

    }
}
