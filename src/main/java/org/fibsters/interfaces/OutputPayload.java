package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

import java.util.List;

public interface OutputPayload {
    public Integer getIndex();
    public void setIndex(Integer index);
    public InputPayload getInputPayload(); // For organization clientside
    public ComputeJobStatus getStatus(); // An in-progress, success, or failure status.

    Boolean isSuccess();

    void setStatus(ComputeJobStatus status);

    List<int[]> getFibCalcResultsInteger2dList();
    void setFibCalcResults(int chunk, int[] fibCalcSubResults, int startIndex, int endIndex);
}