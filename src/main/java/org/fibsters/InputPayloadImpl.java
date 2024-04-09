package org.fibsters;

import com.google.gson.Gson;
import org.fibsters.interfaces.InputPayload;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputPayloadImpl implements InputPayload {

    private String uniqueID;


    public enum DirectiveType {
        GET_JOB_STATUS_BY_ID,
        GET_JOB_BY_ID,
        SUBMIT_COMPUTE_JOB
    }

    private DirectiveType directive;
    private String inputType;
    private String delimiter;
    private PayloadDataImpl payloadData;
    private String outputType;
    private String outputSource;
    private String[] payloadOutputArrayParsed;

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
    /**
     * Static factory method to create an instance of InputPayloadImpl from a JSON string.
     *
     * @param inputString The JSON string to parse.
     * @return An instance of InputPayloadImpl.
     * @throws Exception If there is an error during parsing or validation.
     */
    public static InputPayloadImpl createInputPayloadFromString(String inputString) throws Exception {
        Gson gson = new Gson();
        InputPayloadImpl inputPayload = gson.fromJson(inputString, InputPayloadImpl.class);
        inputPayload.postDeserialize();
        return inputPayload;
    }

    public void postDeserialize() throws Exception {
        switch (this.directive) {
            case GET_JOB_STATUS_BY_ID:
                if (this.uniqueID == null) {
                    throw new Exception("Error: Must have a uniqueID");
                }
                break;
            case GET_JOB_BY_ID:
                if (this.uniqueID == null) {
                    throw new Exception("Error: Must have a uniqueID");
                }
                break;
            case SUBMIT_COMPUTE_JOB:
                if (this.payloadData == null) {
                    throw new Exception("Error: Must have payloadData with CalcFibNumbersUpTo inside it");
                }
                if (this.payloadData.calcFibNumbersUpTo == null) {
                    throw new Exception("Error: Must have CalcFibNumbersUpTo");
                }

                break;
            default:
                throw new Exception("Error: Must be valid directive type" + getListOfDirectiveTypesAsStringLol());
        }
    }

    private String getListOfDirectiveTypesAsStringLol() {
        List<DirectiveType> directiveTypes = Arrays.asList(InputPayloadImpl.DirectiveType.values());
        List<String> directiveTypeNames = directiveTypes.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        return directiveTypeNames.toString();
    }

    /*
    public InputPayloadImpl(JSONObject inputConfig) throws Exception {

        try { // Attempts to convert the JSON object to a valid input payload
            this.uniqueID = inputConfig.getString("uniqueID");
            this.directive = inputConfig.getString("directive");
            this.inputType = inputConfig.getString("inputType");
            this.delimiter = inputConfig.getString("delimiter");
            this.outputType = inputConfig.getString("outputType");
            this.outputSource = inputConfig.getString("outputSource");

            if (Objects.equals(this.inputType, "json")) {
                Object payloadData = inputConfig.getJSONObject("payloadData");

                if (payloadData instanceof JSONObject) {
                    this.payloadDataJSON = (JSONObject) payloadData;
                } else if (payloadData instanceof String) {
                    this.payloadDataJSON = new JSONObject(inputConfig.getString("payloadData"));
                }

                this.payloadDataParsed = new int[this.payloadDataJSON.getJSONArray("CalcFibNumbersUpTo").length()];

                for (int i = 0; i < this.payloadDataJSON.getJSONArray("CalcFibNumbersUpTo").length(); i++) {
                    this.payloadDataParsed[i] = this.payloadDataJSON.getJSONArray("CalcFibNumbersUpTo").getInt(i);
                }

                if (this.payloadDataJSON.has("outputLocations")) {
                    this.payloadOutputArrayParsed = new String[this.payloadDataJSON.getJSONArray("outputLocations").length()];

                    for (int i = 0; i < this.payloadDataJSON.getJSONArray("outputLocations").length(); i++) {
                        this.payloadOutputArrayParsed[i] = this.payloadDataJSON.getJSONArray("outputLocations").getString(i);
                    }
                }

            }
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }


    }
    */
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
        return this.payloadData.calcFibNumbersUpTo.length;
        // Have to calcuate based on if it's csv or json, what fields ect
    }

    @Override
    public DirectiveType getDirective() {
        return this.directive;
    }

    @Override
    public String getDelimiter() {
        return this.delimiter;
    }

    @Override
    public PayloadDataImpl getPayloadData() {
        return this.payloadData;
    }

    @Override
    public int[] getPayloadDataParsed() {
        return payloadData.calcFibNumbersUpTo;
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

    @Override
    public String[] getPayloadOutputArrayParsed() {
        return payloadData.outputLocations;
    }

}
