package org.fibsters.interfaces;

import org.fibsters.InputPayloadImpl;

public interface InputPayload extends Payload {

    void postDeserialize() throws Exception;

    String getUniqueID();

    String getInputType();

    PayloadData getPayloadData();

    int[] getPayloadDataParsed();

    String getOutputType();

    InputPayloadImpl.DirectiveType getDirective();

    String getDelimiter();

    int getTotalSize();

    String[] getPayloadOutputArrayParsed();
}
