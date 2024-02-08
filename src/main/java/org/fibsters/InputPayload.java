package org.fibsters;

interface InputPayload extends Payload {
    public String getUUID();
    public String getInputType();
    private String inputType = "defaultInputType";
    private char delimiter = ',';
    private String payloadData = "defaultPayloadData";
    private String outputType = "defaultOutputType";
    private String outputData = "defaultOutputData";
}