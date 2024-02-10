package org.fibsters.interfaces;

public interface OutputPayload extends Payload {
    public InputPayload getInputPayload(); // For organization clientside
    public ComputeJobStatus getStatus(); // An in-progress, success, or failure status.
}