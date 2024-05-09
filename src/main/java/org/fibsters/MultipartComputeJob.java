package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

public class MultipartComputeJob implements ComputeJob {

    private OutputPayloadImpl outputPayload;

    private ComputeJobStatus status;

    private FibCalcComputeEngineImpl fibCalcCE;
    private FibSpiralComputeEngineImpl fibSpiralCE;

    public MultipartComputeJob(OutputPayloadImpl outputPayload) {
        this.outputPayload = outputPayload;
        this.status = ComputeJobStatus.UNSTARTED;

        this.fibCalcCE = new FibCalcComputeEngineImpl(outputPayload);
        this.fibSpiralCE = new FibSpiralComputeEngineImpl(outputPayload, 0);
    }

    @Override
    public void setInputPayload(InputPayload inputPayload) {
        this.fibCalcCE.setInputPayload(inputPayload);

        this.fibSpiralCE.setInputPayload(inputPayload);
    }

    @Override
    public OutputPayload getOutputPayload() {
        OutputPayload newOutput = this.outputPayload.clone();

        newOutput.setStatus(this.status);

        return newOutput;
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
    public int getTotalSize(int chunk) {
        return this.fibCalcCE.getTotalSize(chunk);
    }

    @Override
    public void setStartIndex(int startIndex) {
        this.fibCalcCE.setStartIndex(startIndex);

        this.fibSpiralCE.setStartIndex(startIndex);
    }

    @Override
    public void setEndIndex(int endIndex) {
        this.fibCalcCE.setEndIndex(endIndex);

        this.fibSpiralCE.setEndIndex(endIndex);
    }

    @Override
    public InputPayload getInputPayload() {
        throw new UnsupportedOperationException("Not supported in multipart.");
    }

    @Override
    public ComputeJob clone() {
        throw new UnsupportedOperationException("Not supported in multipart.");
    }

    @Override
    public void setChunk(int j) {
        throw new UnsupportedOperationException("Not supported in multipart.");
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported in multipart.");
    }

    public boolean isCalcFinished() {
        return this.fibCalcCE.getStatus() == ComputeJobStatus.COMPLETED;
    }

    public boolean isSpiralFinished() {
        return this.fibSpiralCE.getStatus() == ComputeJobStatus.COMPLETED;
    }

    public FibCalcComputeEngineImpl getFibCalcCE() {
        return fibCalcCE;
    }

    public FibSpiralComputeEngineImpl getFibSpiralCE() {
        return fibSpiralCE;
    }

}
