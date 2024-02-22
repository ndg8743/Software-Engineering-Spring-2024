package org.fibsters.interfaces;

import org.fibsters.OutputPayloadImpl;

// TODO: Empty Interface for now. Potentially update with methods in the future or remove and use ComputeJob directly.
public interface FibCalcComputeEngine extends ComputeJob {

    // set/get chunk
    int getChunk();

    void setChunk(int chunk);

    void setOutputPayload(OutputPayloadImpl outputPayload);

}
