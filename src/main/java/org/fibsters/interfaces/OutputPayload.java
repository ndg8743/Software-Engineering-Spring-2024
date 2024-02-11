package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

public interface OutputPayload extends Payload {
    public InputPayload getInputPayload(); // For organization clientside
    public ComputeJobStatus getStatus(); // An in-progress, success, or failure status.
}