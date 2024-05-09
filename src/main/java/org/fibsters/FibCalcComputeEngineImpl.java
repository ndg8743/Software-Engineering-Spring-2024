package org.fibsters;

import org.fibsters.interfaces.FibCalcComputeEngine;
import org.fibsters.interfaces.InputPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FibCalcComputeEngineImpl implements FibCalcComputeEngine {

    private static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2; // Phi
    private ComputeJobStatus status;
    private int startIndex;
    private int endIndex;
    private InputPayload inputPayload; // used for reference potentially(has uuid)
    private int chunk;
    private OutputPayloadImpl outputPayload;
    private static Map<Integer, Integer> memoizationMap; // cache

    public FibCalcComputeEngineImpl(OutputPayloadImpl outputPayload) {
        this.status = ComputeJobStatus.UNSTARTED;
        this.startIndex = 0;
        this.endIndex = 10;
        this.outputPayload = outputPayload;

        memoizationMap = new ConcurrentHashMap<>();
    }

    @Override
    public FibCalcComputeEngineImpl clone() {
        FibCalcComputeEngineImpl clone = new FibCalcComputeEngineImpl(this.outputPayload);

        clone.setStatus(this.status);
        clone.setStartIndex(this.startIndex);
        clone.setEndIndex(this.endIndex);
        clone.setInputPayload(this.inputPayload);
        clone.setOutputPayload(this.outputPayload);
        clone.setChunk(this.chunk);

        return clone;
    }

    @Override
    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    // set/get chunk
    @Override
    public int getChunk() {
        return chunk;
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
    public int getTotalSize() {
        int sum = 0;

        for (int num : inputPayload.getPayloadDataParsed()) {
            sum += num;
        }

        return sum;
    }

    @Override
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public void setEndIndex(int endIndex) {
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

        if (total < 0) {
            throw new IllegalArgumentException("startIndex must be less than or equal to endIndex");
        }

        int[] fibonacci = new int[total];

        if (total > 0) {
            fibonacci[0] = calculateNthFibonacci(startIndex);
        }

        if (total > 1) {
            //1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584 4181 6765 10946 17711 28657 46368 75025 121393 196418 317811 514229
            Map<Integer, Integer> memoizationMap = new HashMap<>();

            // Store the first two Fibonacci numbers directly
            memoizationMap.put(0, calculateNthFibonacci(startIndex));
            memoizationMap.put(1, calculateNthFibonacci(startIndex + 1));

            for (int i = 2; i < total; i++) {
                int next = memoizationMap.get(i - 1) + memoizationMap.get(i - 2);
                memoizationMap.put(i, next);
            }

            // Fill the fibonacci array
            for (int i = 0; i < total; i++) {
                fibonacci[i] = memoizationMap.get(i);
            }
/*            fibonacci[1] = calculateNthFibonacci(startIndex + 1);

            for (int i = startIndex + 2; i < endIndex; i++) {
                fibonacci[i - startIndex] = fibonacci[i - 1 - startIndex] + fibonacci[i - 2 - startIndex];
                //System.err.println(fibonacci[i-startIndex]);
            }*/
        }

        outputPayload.setFibCalcResults(this.chunk, fibonacci, startIndex, endIndex);

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
