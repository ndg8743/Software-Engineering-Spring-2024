package org.fibsters.interfaces;

import org.json.JSONObject;

interface Payload {
    public String getDelimiter();
    public JSONObject getPayloadData();
    public String getOutputType();
    public String getOutputSource(); // is a path to a file, a json object, a csv string, a database connection, ...
    public void printPayload(); // print the payload to the console
}




