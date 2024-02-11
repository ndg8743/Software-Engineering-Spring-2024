package org.fibsters;

import org.fibsters.ComputeJobStatus;
import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.FibCalcComputeEngine;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

public class FibCalcComputeEngineImpl implements FibCalcComputeEngine {
    private static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2; // Phi
    private int[] fibonacci;
    ComputeJobStatus status;
    Integer startIndex;
    Integer endIndex;
    InputPayload inputPayload; // used for reference potentially(has uuid)
    Integer chunkSize;

    OutputPayloadImpl outputPayload;

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
    public OutputPayloadImpl getOutputPayload() {
        return this.outputPayload;
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
        // TODO: report failure if that's possible
        // TODO: Do proper chunking and offsetting, need to hold off on implementation for now
        /*
        status = ComputeJobStatus.PENDING;
        int total = endIndex - startIndex;
        fibonacci = new int[total];
        fibonacci[0] = calculateNthFibonacci(startIndex);
        fibonacci[1] = calculateNthFibonacci(startIndex + 1);
        for (int i = startIndex + 2; i < endIndex; i++) {
            fibonacci[i - startIndex] = fibonacci[i - 1 - startIndex] + fibonacci[i - 2 - startIndex];
            System.err.println(fibonacci[i-startIndex]);
        }
        outputPayload = new OutputPayloadImpl(startIndex, inputPayload, ComputeJobStatus.COMPLETED);
        status = ComputeJobStatus.COMPLETED;
        */
    }

    public static int calculateNthFibonacci(int n) {
        assert n >= 0;

        return (int) Math.round(Math.pow(GOLDEN_RATIO, n + 1) / Math.sqrt(5));
    }

    @Override
    public void setOutputPayload(OutputPayloadImpl outputPayload) {
        this.outputPayload = outputPayload;
    }
}
