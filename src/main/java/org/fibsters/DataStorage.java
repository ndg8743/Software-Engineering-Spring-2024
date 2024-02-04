package org.fibsters;

interface DataStorage {
    // Sanitizes and validates the input and output payloads.
    public Result<InputPayload> parseInputPayload(String inputPayloadString);
    public Result<OutputPayload> parseOutputPayload(ComputeJob completedJob);

    // Saving the payload to the database or to a file.
    Result<OutputPayload> saveToDatabase(InputPayload outputPayload);
    Result<OutputPayload> saveToFile(InputPayload outputPayload); // handle csv, json, xml, png, etc. might need to be multiple methods

    // load
    Result<InputPayload> loadFromDatabase(InputPayload inputPayload);
    Result<InputPayload> loadFromFile(InputPayload inputPayload);

}
