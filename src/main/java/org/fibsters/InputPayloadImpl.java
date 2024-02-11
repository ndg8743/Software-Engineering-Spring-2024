package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.json.JSONObject;

import java.util.Objects;

public class InputPayloadImpl implements InputPayload {
    String uniqueID;
    String inputType;
    String delimiter;
    JSONObject payloadData;
    String outputType;
    String outputSource;

    /**
     * Constructor for InputPayloadImpl
     * @param inputConfig
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
    public InputPayloadImpl(JSONObject inputConfig) throws Exception {
        try { // Attempts to convert the JSON object to a valid input payload
            this.uniqueID = inputConfig.getString("uniqueID");
            this.inputType = inputConfig.getString("inputType");
            this.delimiter = inputConfig.getString("delimiter");
            this.outputType = inputConfig.getString("outputType");
            this.outputSource = inputConfig.getString("outputSource");
            if (Objects.equals(this.inputType, "json")) {
                String payloadDataString = inputConfig.getString("payloadData");
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
        return this.outputSource;
    }

    @Override
    public void printPayload() {

    }
}
