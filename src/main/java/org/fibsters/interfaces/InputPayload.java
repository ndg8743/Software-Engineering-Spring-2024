package org.fibsters.interfaces;

import org.json.JSONObject;

public interface InputPayload extends Payload {

    String getUniqueID();

    String getInputType();

    JSONObject getPayloadData();

    String getOutputType();

    String getOutputData();

    String getDelimiter();

    int getTotalSize();

}
