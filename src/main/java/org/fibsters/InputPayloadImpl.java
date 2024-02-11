package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.json.JSONObject;

public class InputPayloadImpl implements InputPayload {
    String uniqueID;
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
        "uniqueID": "1234",
        "inputType": "json",
        "delimiter": ",",
        "outputType": "json",
        "outputSource": "output.json"
    }
     */
    public InputPayloadImpl(JSONObject inputPayload) throws Exception {
        try { // Attempts to convert the JSON object to a valid input payload
            this.uniqueID = inputPayload.getString("uniqueID");
            this.inputType = inputPayload.getString("inputType");
            this.delimiter = inputPayload.getString("delimiter");
            this.outputType = inputPayload.getString("outputType");
            this.outputSource = inputPayload.getString("outputSource");
            if(this.inputType == "json") {
                String payloadDataString = inputPayload.getString("payloadData");
                this.payloadData = new JSONObject(payloadDataString);
            }
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }
    @Override
    public String getUniqueID() {
        return this.uniqueID;
    }

    @Override
    public String getInputType() {
        return this.inputType;
    }

    // parse data to get total size.
    // used to split up the work for the compute engines
    @Override
    public Integer getTotalSize() {
        return null; // Have to calcuate based on if it's csv or json, what fields ect
    }

    @Override
    public String getDelimiter() {
        return this.delimiter;
    }

    @Override
    public JSONObject getPayloadData() {
        return this.payloadData;
    }

    @Override
    public String getOutputType() {
        return this.outputType;
    }

    @Override
    public String getOutputData() {
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
