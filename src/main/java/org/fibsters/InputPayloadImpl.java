package org.fibsters;

import com.google.gson.Gson;
import org.fibsters.interfaces.InputPayload;

import java.util.Arrays;
import java.util.List;

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

    @Override
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
                    throw new Exception("Error: Must have payloadData with calcFibNumbersUpTo inside it");
                }

                if (this.payloadData.calcFibNumbersUpTo == null) {
                    throw new Exception("Error: Must have calcFibNumbersUpTo");
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
                .toList();

        return directiveTypeNames.toString();
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
        // Have to calculate based on if it's csv or json, what fields ect
        return this.payloadData.calcFibNumbersUpTo.length;
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
    public String getOutputSource() {
        return this.outputSource;
    }

    @Override
    public String[] getPayloadOutputArrayParsed() {
        return payloadData.outputLocations;
    }

}
