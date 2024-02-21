package org.fibsters;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputPayloadImpl implements OutputPayload {
    int index;
    InputPayload inputPayload;
    ComputeJobStatus status;
    List<int[]> fibCalcResults;
    List<List<String>> fibCalcStrings;



    public OutputPayloadImpl(int index, InputPayload inputPayload, ComputeJobStatus status) {
        this.index = index;
        this.inputPayload = inputPayload;
        this.status = status;
        int totalSize = inputPayload.getTotalSize();
        int[] payloadDataParsed = inputPayload.getPayloadDataParsed();
        this.fibCalcResults = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            this.fibCalcResults.add(new int[payloadDataParsed[i]]);
        }
        this.fibCalcStrings = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            this.fibCalcStrings.add(new ArrayList<>(payloadDataParsed.length));
        }
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
        return this.status;
    }

    @Override
    public Boolean isSuccess() {
        return this.status.equals(ComputeJobStatus.COMPLETED);
    }

    @Override
    public void setStatus(ComputeJobStatus status) {
        this.status = status;
    }

    @Override
    public List<int[]> getFibCalcResultsInteger2dList() {
        return this.fibCalcResults;
    }

    @Override
    public void setFibCalcResults(int chunk, int[] fibCalcSubResults, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            int relativeIndex = i - startIndex;
            this.fibCalcResults.get(chunk)[i] = fibCalcSubResults[relativeIndex];
        }
        //System.out.println(chunk +" fibCalcResults: " + Arrays.toString(fibCalcSubResults) + " startindex: " + startIndex);
        //System.out.println("Updated Chunk: " + Arrays.toString(this.fibCalcResults.get(chunk)));
    }

    public List<String> toStringList() {
        ArrayList<String> jsonStringList = new ArrayList<>();

        for (int[] secondList : this.fibCalcResults) {
            for (Integer integer : secondList) {
                jsonStringList.add(Integer.toString(integer));
            }
        }

        return jsonStringList;
    }

}
