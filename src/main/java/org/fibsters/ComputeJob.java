package org.fibsters;

interface ComputeJob extends Runnable {
    void setInputPayload(InputPayload inputPayload);

    void getInputPayload();

}
