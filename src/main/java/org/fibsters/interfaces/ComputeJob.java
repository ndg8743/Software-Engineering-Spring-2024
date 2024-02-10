package org.fibsters.interfaces;

public interface ComputeJob extends Runnable {
    void setInputPayload(InputPayload inputPayload);

    InputPayload getInputPayload();

}
