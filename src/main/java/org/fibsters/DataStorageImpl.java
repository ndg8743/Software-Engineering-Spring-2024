package org.fibsters;

import io.grpc.stub.StreamObserver;
import org.fibsters.InputPayloadRequestOuterClass.InputPayloadRequest;
import org.fibsters.InputPayloadMessageOuterClass.InputPayloadMessage;
import org.fibsters.InputPayloadResponseOuterClass.InputPayloadResponse;
import org.fibsters.InputPayloadMessageOuterClass.PayloadDataImpl;
import org.fibsters.interfaces.DataStorage;
import org.fibsters.InputPayloadServiceGrpc.InputPayloadServiceImplBase;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.Result;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.interfaces.ComputeJob;

import java.util.Arrays;

public class DataStorageImpl implements DataStorage {

    @Override
    public Result<InputPayloadImpl> parseInputPayload(String inputPayloadString) {
        // Attempt to create an InputPayload object from the JSON object(keys need to match up/data types need to match up)
        try {
            InputPayloadImpl inputPayload = InputPayloadImpl.createInputPayloadFromString(inputPayloadString);

            return new SuccessResult<>(inputPayload);
        } catch (Exception e) {
            return new FailureResult<>(Arrays.toString(e.getStackTrace()), "Input JSON not in correct format " + e.getMessage());
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
