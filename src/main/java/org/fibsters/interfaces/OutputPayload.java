package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

import java.awt.image.BufferedImage;
import java.util.List;

public interface OutputPayload {

    int getIndex();

    void setIndex(int index);

    InputPayload getInputPayload(); // For organization clientside

    ComputeJobStatus getStatus(); // An in-progress, success, or failure status.

    Boolean isSuccess();

    void setStatus(ComputeJobStatus status);

    List<int[]> getFibCalcResultsInteger2dList();

    void setFibCalcResults(int chunk, int[] fibCalcSubResults, int startIndex, int endIndex);

    String getUniqueID();

    BufferedImage getOutputImage();

    Object toJSON();
}