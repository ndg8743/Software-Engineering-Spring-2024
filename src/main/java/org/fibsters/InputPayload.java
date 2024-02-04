package org.fibsters;

interface InputPayload extends Payload {
    public String getUUID();
    public String getInputType();
}