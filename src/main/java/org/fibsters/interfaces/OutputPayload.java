package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

import java.util.List;

public interface OutputPayload {
    public Integer getIndex();
    public void setIndex(Integer index);
    public InputPayload getInputPayload(); // For organization clientside
    public ComputeJobStatus getStatus(); // An in-progress, success, or failure status.

    List<List<Integer>> getFibCalcResultsInteger2dList();

    public void setFibCalcResults(List<List<Integer>> fibCalcResults);
}