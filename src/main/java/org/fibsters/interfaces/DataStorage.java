package org.fibsters.interfaces;

public interface DataStorage {

    // Sanitizes and validates the input and output payloads.
    Result<InputPayload> parseInputPayload(String inputPayloadString);

    Result<OutputPayload> parseOutputPayload(ComputeJob completedJob);

    // public Save/load methods. Calls internal methods
    Result<OutputPayload> save(InputPayload outputPayload);

    Result<InputPayload> load(InputPayload inputPayload);

    // Saving the payload to the database or to a file.
    Result<OutputPayload> saveToDatabase(InputPayload outputPayload);

    Result<OutputPayload> saveToFile(InputPayload inputPayload); // handle csv, json, xml, png, etc. might need to be multiple methods

    // load
    Result<InputPayload> loadFromDatabase(InputPayload inputPayload);

    Result<InputPayload> loadFromFile(InputPayload inputPayload);

}
