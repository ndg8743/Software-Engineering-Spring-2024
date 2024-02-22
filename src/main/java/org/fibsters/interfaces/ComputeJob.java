package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

public interface ComputeJob extends Runnable {

    void setInputPayload(InputPayload inputPayload);

    OutputPayload getOutputPayload();

    ComputeJobStatus getStatus();

    void setStatus(ComputeJobStatus status);

    int getTotalSize();

    void setStartIndex(int startIndex);

    void setEndIndex(int endIndex);

    InputPayload getInputPayload();

    ComputeJob clone();

    void setChunk(int j);

}
