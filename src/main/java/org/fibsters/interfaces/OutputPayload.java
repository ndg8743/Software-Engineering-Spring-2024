package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

import java.util.List;

public interface OutputPayload {

    int getIndex();

    void setIndex(int index);

    InputPayload getInputPayload(); // For organization clientside

    ComputeJobStatus getStatus(); // An in-progress, success, or failure status.

    List<List<Integer>> getFibCalcResultsInteger2dList();

    void setFibCalcResults(List<List<Integer>> fibCalcResults);

}