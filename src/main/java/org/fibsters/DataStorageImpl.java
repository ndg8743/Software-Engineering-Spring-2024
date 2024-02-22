package org.fibsters;

import org.fibsters.interfaces.DataStorage;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.Result;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.interfaces.ComputeJob;
import org.json.JSONObject;

import org.json.JSONException;

import java.util.Arrays;

public class DataStorageImpl implements DataStorage {

    @Override
    public Result<InputPayload> parseInputPayload(String inputPayloadString) {
        JSONObject inputPayloadJson;

        // Attempt to parse the JSON string into a JSON object
        try {
            inputPayloadJson = new JSONObject(inputPayloadString);
        } catch (JSONException e) {
            return new FailureResult<>(Arrays.toString(e.getStackTrace()),"Input is not JSON: " + e.getMessage());
        }

        // Attempt to create an InputPayload object from the JSON object(keys need to match up/data types need to match up)
        try {
            InputPayload inputPayload = new InputPayloadImpl(inputPayloadJson);

            return new SuccessResult<>(inputPayload);
        } catch (Exception e) {
            return new FailureResult<>(Arrays.toString(e.getStackTrace()),"Input JSON not in correct format " + e.getMessage());
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
