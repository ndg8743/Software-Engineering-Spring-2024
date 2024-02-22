package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

import java.util.ArrayList;
import java.util.List;

public class OutputPayloadImpl implements OutputPayload {

    private final int index;
    private final InputPayload inputPayload;
    private final ComputeJobStatus status;
    private List<List<Integer>> fibCalcResults;
    private List<List<String>> fibCalcStrings;

    public OutputPayloadImpl(int index, InputPayload inputPayload, ComputeJobStatus status) {
        this.index = index;
        this.inputPayload = inputPayload;
        this.status = status;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public void setIndex(int index) {

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
    public List<List<Integer>> getFibCalcResultsInteger2dList() {
        return this.fibCalcResults;
    }

    @Override
    public void setFibCalcResults(List<List<Integer>> fibCalcResults) {
        this.fibCalcResults = fibCalcResults;
    }

    public List<String> toStringList() {
        ArrayList<String> jsonStringList = new ArrayList<>();

        for (List<Integer> secondList : this.fibCalcResults) {
            for (Integer integer : secondList) {
                jsonStringList.add(Integer.toString(integer));
            }
        }

        return jsonStringList;
    }

}
