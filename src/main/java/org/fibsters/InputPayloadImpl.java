package org.fibsters;

import org.fibsters.interfaces.InputPayload;
import org.json.JSONObject;

import java.util.Objects;

public class InputPayloadImpl implements InputPayload {

    private String uniqueID;
    private String inputType;
    private String delimiter;
    private JSONObject payloadDataJSON;
    private String outputType;
    private String outputSource;
    private int[] payloadDataParsed;

    /**
     * Constructor for InputPayloadImpl
     *
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
                this.payloadDataJSON = new JSONObject(payloadDataString);
                this.payloadDataParsed = new int[this.payloadDataJSON.getJSONArray("CalcFibNumbersUpTo").length()];

                for (int i = 0; i < this.payloadDataJSON.getJSONArray("CalcFibNumbersUpTo").length(); i++) {
                    this.payloadDataParsed[i] = this.payloadDataJSON.getJSONArray("CalcFibNumbersUpTo").getInt(i);
                }
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
    public int getTotalSize() {
        // TODO: Make this consider the input type
        return this.payloadDataParsed.length;
        // Have to calcuate based on if it's csv or json, what fields ect
    }

    @Override
    public String getDelimiter() {
        return this.delimiter;
    }

    @Override
    public JSONObject getPayloadData() {
        return this.payloadDataJSON;
    }

    @Override
    public int[] getPayloadDataParsed() {
        return payloadDataParsed;
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
