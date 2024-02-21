package org.fibsters.interfaces;

import org.json.JSONObject;

public interface InputPayload extends Payload {
    public String getUniqueID();
    public String getInputType();
    JSONObject getPayloadData();

    int[] getPayloadDataParsed();

    String getOutputType();
    String getOutputData();
    String getDelimiter();
    public Integer getTotalSize();
}
