package org.fibsters;
import org.fibsters.ComputeJobStatus;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.json.JSONObject;

import java.util.List;

public class OutputPayloadImpl implements OutputPayload {
    int index;
    InputPayload inputPayload;
    ComputeJobStatus status;
    List<List<Integer>> fibCalcResults;



    public OutputPayloadImpl(int index, InputPayload inputPayload, ComputeJobStatus status) {
        this.index = index;
        this.inputPayload = inputPayload;
        this.status = status;
    }
    @Override
    public Integer getIndex() {
        return null;
    }

    @Override
    public void setIndex(Integer index) {

    }

    @Override
    public InputPayload getInputPayload() {
        return null;
    }

    @Override
    public ComputeJobStatus getStatus() {
        return null;
    }

    @Override
    public List<List<Integer>> getFibCalcResults() {
        return null;
    }

    @Override
    public void setFibCalcResults(List<List<Integer>> fibCalcResults) {

    }

}
