package org.fibsters;

interface Payload {
    public String getDelimiter();
    public String getPayloadData();
    public String getOutputType();
    public String getOutputSource(); // is a path to a file, a json object, a csv string, a database connection, ...
}




