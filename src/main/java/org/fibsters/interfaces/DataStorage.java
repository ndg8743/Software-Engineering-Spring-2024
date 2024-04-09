package org.fibsters.interfaces;

import org.fibsters.InputPayloadImpl;

public interface DataStorage {

    // Sanitizes and validates the input and output payloads.
    Result<InputPayloadImpl> parseInputPayload(String inputPayloadString);

    Result<OutputPayload> parseOutputPayload(ComputeJob completedJob);

    // public Save/load methods. Calls internal methods
    Result<OutputPayload> save(InputPayload outputPayload);

    Result<InputPayload> load(InputPayload inputPayload);

    Result<OutputPayload> saveToDatabase(InputPayload outputPayload);

    Result<OutputPayload> saveToFile(InputPayload inputPayload); // handle csv, json, xml, png, etc. might need to be multiple methods

    Result<InputPayload> loadFromDatabase(InputPayload inputPayload);

    Result<InputPayload> loadFromFile(InputPayload inputPayload);

}
