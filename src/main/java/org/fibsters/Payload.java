package org.fibsters;

interface Payload {
    public String getDelimiter();
    public String getPayloadData();
    public String getOutputType();
    public String getOutputSource(); // is a path to a file, a json object, a csv string, a database connection, ...
}

interface InputPayload extends Payload {
    public String getUUID();
    public String getInputType();
}

interface OutputPayload extends Payload {
    public InputPayload getInputPayload(); // For organization clientside
    public ComputeJobStatus getStatus(); // An in-progress, success, or failure status.

}
