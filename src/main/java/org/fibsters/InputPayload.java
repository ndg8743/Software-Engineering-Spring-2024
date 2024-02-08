package org.fibsters;

interface InputPayload extends Payload {
    public String getUUID();
    public String getInputType();
    String inputType = "defaultInputType";
    char delimiter = ',';
    String payloadData = "defaultPayloadData";
    String outputType = "defaultOutputType";
    String outputData = "defaultOutputData";
}
