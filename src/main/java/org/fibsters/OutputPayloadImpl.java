package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.util.UniqueIDGenerator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OutputPayloadImpl implements OutputPayload {

    private String uniqueID;
    private int index;
    private InputPayloadImpl inputPayload;
    private ComputeJobStatus status;
    private List<int[]> fibCalcResults;

    // need the output for the spiral as a variable here
    private CopyOnWriteArrayList<BufferedImage> fibSpiralResults;
    private List<List<String>> fibCalcStrings;

    public OutputPayloadImpl(int index, InputPayloadImpl inputPayload, ComputeJobStatus status) {
        // set id to random number
        this.uniqueID = UniqueIDGenerator.generateUniqueID();
        this.index = index;
        this.inputPayload = inputPayload;
        this.status = status;
        int totalSize = inputPayload.getTotalSize();
        int[] payloadDataParsed = inputPayload.getPayloadDataParsed();
        this.fibCalcResults = new ArrayList<>();

        // need to parse the height/width
        // temporarily set it as whatever
        this.fibSpiralResults = new CopyOnWriteArrayList<>();

        for (int i = 0; i < totalSize; i++) {
            this.fibCalcResults.add(new int[payloadDataParsed[i]]);
        }

        this.fibCalcStrings = new ArrayList<>();

        for (int i = 0; i < totalSize; i++) {
            this.fibCalcStrings.add(new ArrayList<>(payloadDataParsed.length));
        }

        int chunks = fibCalcResults.size();

        for (int i = 0; i < chunks; i++) {
            this.fibSpiralResults.add(new BufferedImage(FibSpiralComputeEngineImpl.WIDTH, FibSpiralComputeEngineImpl.HEIGHT, BufferedImage.TYPE_INT_RGB));
        }
    }

    @Override
    public String getUniqueID() {
        return this.uniqueID;
    }

    protected void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    @Override
    public BufferedImage getOutputImage(int chunk) {
        return this.fibSpiralResults.get(chunk);
    }

    @Override
    public Object toJSON() {
        return null;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
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

    public String[] getFileLocations() {
        return this.inputPayload.getPayloadOutputArrayParsed();
    }

    public OutputPayloadImpl clone() {
        OutputPayloadImpl outputPayload = new OutputPayloadImpl(this.index, this.inputPayload, this.status);

        outputPayload.setUniqueID(this.uniqueID);
        outputPayload.setIndex(this.index);
        outputPayload.fibCalcResults = this.fibCalcResults;
        outputPayload.fibSpiralResults = this.fibSpiralResults;
        outputPayload.fibCalcStrings = this.fibCalcStrings;

        return outputPayload;
    }

}
