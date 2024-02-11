package org.fibsters;

import org.fibsters.interfaces.*;
import org.json.JSONObject;

import org.json.JSONException;
import org.json.JSONObject;

public class DataStorageImpl implements DataStorage {
    @Override
    public Result<InputPayload> parseInputPayload(String inputPayloadString) {
        JSONObject inputPayloadJson;
        // Attempt to parse the JSON string into a JSON object
        try {
            inputPayloadJson = new JSONObject(inputPayloadString);
        } catch (JSONException e) {
            FailureResult result = new FailureResult<>(e.getStackTrace().toString(),"Input is not JSON: " + e.getMessage());
            return result;
        }
        // Attempt to create an InputPayload object from the JSON object(keys need to match up/data types need to match up)
        try{
            InputPayload inputPayload = new InputPayloadImpl(inputPayloadJson);
            SuccessResult result = new SuccessResult<>(inputPayload);
            return result;
        } catch (Exception e) {
            FailureResult result = new FailureResult<>(e.getStackTrace().toString(),"Input JSON not in correct format " + e.getMessage());
            return result;
        }
    }

    @Override
    public Result<OutputPayload> parseOutputPayload(ComputeJob completedJob) {
        return null;
    }

    @Override
    public Result<OutputPayload> save(InputPayload outputPayload) {
        return null;
    }

    @Override
    public Result<InputPayload> load(InputPayload inputPayload) {
        return null;
    }

    @Override
    public Result<OutputPayload> saveToDatabase(InputPayload outputPayload) {
        return null;
    }

    @Override
    public Result<OutputPayload> saveToFile(InputPayload outputPayload) {
        return null;
    }

    @Override
    public Result<InputPayload> loadFromDatabase(InputPayload inputPayload) {
        return null;
    }

    @Override
    public Result<InputPayload> loadFromFile(InputPayload inputPayload) {
        return null;
    }
}
