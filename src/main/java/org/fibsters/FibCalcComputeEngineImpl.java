package org.fibsters;

import org.fibsters.interfaces.FibCalcComputeEngine;
import org.fibsters.interfaces.InputPayload;

public class FibCalcComputeEngineImpl implements FibCalcComputeEngine {
    private static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2; // Phi
    private int[] fibonacci;
    ComputeJobStatus status;
    Integer startIndex;
    Integer endIndex;
    InputPayload inputPayload; // used for reference potentially(has uuid)
    Integer chunk;

    OutputPayloadImpl outputPayload;

    public FibCalcComputeEngineImpl(OutputPayloadImpl outputPayload) {
        status = ComputeJobStatus.UNSTARTED;
        startIndex = 0;
        endIndex = 0;
        this.outputPayload = outputPayload;
    }
    @Override
    public FibCalcComputeEngineImpl clone() {
        FibCalcComputeEngineImpl c = new FibCalcComputeEngineImpl(this.outputPayload);
        c.setStatus(this.status);
        c.setStartIndex(this.startIndex);
        c.setEndIndex(this.endIndex);
        c.setInputPayload(this.inputPayload);
        c.setOutputPayload(this.outputPayload);
        c.setChunk(this.chunk);
        return c;
    }

    @Override
    public void setChunk(int j) {
        this.chunk = j;
    }

    // set/get chunk
    @Override
    public Integer getChunk() {
        return chunk;
    }

    @Override
    public void setChunk(Integer chunk) {
        this.chunk = chunk;
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
        this.outputPayload.setStatus(status);
        this.status = status;
    }

    @Override
    public Integer getTotalSize() {
        int sum = 0;
        for (int num : inputPayload.getPayloadDataParsed()) {
            sum += num;
        }
        return sum;
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

        status = ComputeJobStatus.PENDING;
        int total = endIndex - startIndex;
        fibonacci = new int[total];
        if (total == 1) {
            fibonacci[0] = calculateNthFibonacci(startIndex);
        } else {
            fibonacci[0] = calculateNthFibonacci(startIndex);
            fibonacci[1] = calculateNthFibonacci(startIndex + 1);
            for (int i = startIndex + 2; i < endIndex; i++) {
                fibonacci[i - startIndex] = fibonacci[i - 1 - startIndex] + fibonacci[i - 2 - startIndex];
                //System.err.println(fibonacci[i-startIndex]);
            }
        }
        //outputPayload = new OutputPayloadImpl(startIndex, inputPayload, ComputeJobStatus.COMPLETED);
        outputPayload.setFibCalcResults(this.chunk, fibonacci, startIndex, endIndex);
        //System.out.println(this.chunk + " FibCalcComputeEngineImpl.run() completed " + startIndex + " " + endIndex);
        status = ComputeJobStatus.COMPLETED;

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
