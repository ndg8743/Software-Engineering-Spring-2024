package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.json.JSONObject;

public class InputPayloadImpl implements InputPayload {
    String UUID;
    String inputType;
    String delimiter;
    JSONObject payloadData;
    String outputType;
    String outputSource;

    /**
     * Constructor for InputPayloadImpl
     * @param inputPayload
     * @throws Exception
     */
    /*
    Example correct JSON input:
    {
        "UUID": "1234",
        "inputType": "ccsv",
        "delimiter": ",",
        "outputType": "json",
        "outputSource": "output.json"
    }
     */
    public InputPayloadImpl(JSONObject inputPayload) throws Exception {
        try{ // Attempts to convert the JSON object to a valid input payload
            this.UUID = inputPayload.getString("UUID");
            this.inputType = inputPayload.getString("inputType");
            this.delimiter = inputPayload.getString("delimiter");
            this.outputType = inputPayload.getString("outputType");
            this.outputSource = inputPayload.getString("outputSource");
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
        this.payloadData = inputPayload;
    }
    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public String getInputType() {
        return null;
    }

    @Override
    public String getDelimiter() {
        return null;
    }

    @Override
    public JSONObject getPayloadData() {
        return null;
    }

    @Override
    public String getOutputType() {
        return null;
    }

    @Override
    public String getOutputSource() {
        return null;
    }

    @Override
    public void printPayload() {

    }
}
