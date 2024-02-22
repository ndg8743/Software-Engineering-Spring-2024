package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.FibSpiralComputeEngine;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

import java.awt.image.BufferedImage;

public class FibSpiralComputeEngineImpl implements FibSpiralComputeEngine {

    private ComputeJobStatus status;
    private int[] fibonacci;
    private int startIndex;
    private int endIndex;
    private InputPayload inputPayload; // used for reference potentially(has uuid)

    private OutputPayloadImpl outputPayload;

    private int chunk;

    public FibSpiralComputeEngineImpl(OutputPayloadImpl outputPayload) {
        this.status = ComputeJobStatus.UNSTARTED;
        this.startIndex = 0;
        this.endIndex = 0;
        this.outputPayload = outputPayload;
    }

    @Override
    public void setInputPayload(InputPayload inputPayload) {
        this.inputPayload = inputPayload;
    }

    @Override
    public OutputPayload getOutputPayload() {
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
    public int getTotalSize() {
        return this.fibonacci.length;
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
    public ComputeJob clone() {
        FibSpiralComputeEngineImpl clone = new FibSpiralComputeEngineImpl(this.outputPayload);

        clone.setStatus(this.status);
        clone.setStartIndex(this.startIndex);
        clone.setEndIndex(this.endIndex);
        clone.setInputPayload(this.inputPayload);
        clone.setOutputPayload(this.outputPayload);
        clone.setChunk(this.chunk);

        return clone;
    }

    @Override
    public int getChunk() {
        return this.chunk;
    }

    @Override
    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    @Override
    public void setOutputPayload(OutputPayloadImpl outputPayload) {
        this.outputPayload = outputPayload;
    }

    @Override
    public void run() {
        this.status = ComputeJobStatus.RUNNING;
        BufferedImage image = this.outputPayload.getOutputImage(); //image to write to
        //TODO: do the writing that's in the legacy class


        this.status = ComputeJobStatus.COMPLETED;
    }

}
