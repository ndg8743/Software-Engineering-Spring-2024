package org.fibsters.interfaces;

import org.json.JSONObject;

public interface Payload {

    String getDelimiter();

    JSONObject getPayloadData();

    String getOutputType();

    String getOutputSource(); // is a path to a file, a json object, a csv string, a database connection, ...

    void printPayload(); // print the payload to the console

}




