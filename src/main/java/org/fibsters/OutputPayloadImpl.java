package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputPayloadImpl implements OutputPayload {

    private int index;
    private InputPayload inputPayload;
    private ComputeJobStatus status;
    private List<int[]> fibCalcResults;

    // need the output for the spiral as a variable here
    private BufferedImage fibSpiralResult;
    private List<List<String>> fibCalcStrings;

    public OutputPayloadImpl(int index, InputPayload inputPayload, ComputeJobStatus status) {
        this.index = index;
        this.inputPayload = inputPayload;
        this.status = status;
        int totalSize = inputPayload.getTotalSize();
        int[] payloadDataParsed = inputPayload.getPayloadDataParsed();
        this.fibCalcResults = new ArrayList<>();

        // need to parse the height/width
        // temporarily set it as whatever
        this.fibSpiralResult = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < totalSize; i++) {
            this.fibCalcResults.add(new int[payloadDataParsed[i]]);
        }

        this.fibCalcStrings = new ArrayList<>();

        for (int i = 0; i < totalSize; i++) {
            this.fibCalcStrings.add(new ArrayList<>(payloadDataParsed.length));
        }
    }

    @Override
    public BufferedImage getOutputImage(){
        return this.fibSpiralResult;
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

        System.out.println(chunk +" fibCalcResults: " + Arrays.toString(fibCalcSubResults) + " startindex: " + startIndex);
        System.out.println("Updated Chunk: " + Arrays.toString(this.fibCalcResults.get(chunk)));
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
