package org.fibsters.interfaces;

public interface Payload {

    String getDelimiter();

    PayloadData getPayloadData();

    String getOutputType();

    String getOutputSource(); // is a path to a file, a json object, a csv string, a database connection, ...

}




