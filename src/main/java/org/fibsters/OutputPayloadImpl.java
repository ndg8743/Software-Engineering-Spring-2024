package org.fibsters;
import org.fibsters.interfaces.ComputeJobStatus;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.json.JSONObject;

public class OutputPayloadImpl implements OutputPayload {
    @Override
    public InputPayload getInputPayload() {
        return null;
    }

    @Override
    public ComputeJobStatus getStatus() {
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
