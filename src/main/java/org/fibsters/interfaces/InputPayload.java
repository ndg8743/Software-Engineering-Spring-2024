package org.fibsters.interfaces;

import org.fibsters.InputPayloadImpl;

public interface InputPayload extends Payload {

    String getUniqueID();

    String getInputType();

    PayloadData getPayloadData();

    int[] getPayloadDataParsed();

    String getOutputType();

    String getOutputData();

    InputPayloadImpl.DirectiveType getDirective();

    String getDelimiter();

    int getTotalSize();

    String[] getPayloadOutputArrayParsed();
}
