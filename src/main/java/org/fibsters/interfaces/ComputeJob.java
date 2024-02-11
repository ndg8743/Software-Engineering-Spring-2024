package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

public interface ComputeJob extends Runnable {
    void setInputPayload(InputPayload inputPayload);
    OutputPayload getOutputPayload();

    ComputeJobStatus getStatus();

    void setStatus(ComputeJobStatus status);

    Integer getTotalSize();
    void setStartIndex(Integer startIndex);

    void setEndIndex(Integer endIndex);

    InputPayload getInputPayload();

}
