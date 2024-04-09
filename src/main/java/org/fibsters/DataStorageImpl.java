package org.fibsters;

import com.google.protobuf.Any;
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
import java.util.List;

public class DataStorageImpl extends InputPayloadServiceImplBase implements DataStorage {

    private InputPayloadMessage getMessageFromPayload(InputPayloadImpl payload) {

        List<Integer> calcFibNumbersUpToList = Arrays.asList(Arrays.stream(payload.getPayloadDataParsed()).boxed().toArray(Integer[]::new));

        PayloadDataImpl payloadMessage = PayloadDataImpl.newBuilder()
                .addAllCalcFibNumbersUpTo(calcFibNumbersUpToList).build();


        InputPayloadMessage message = InputPayloadMessage.newBuilder()
                .setPayloadData(payloadMessage) // public Builder setPayloadData(org.fibsters.InputPayloadMessageOuterClass.PayloadDataImpl value) {
                .build();

        return message;
    }

    @Override
    public void parseMessage(InputPayloadRequest request, StreamObserver<InputPayloadResponse> responseObserver) {
        InputPayloadResponse response;

        //call parseInputPayload(String inputPayloadString)

        String inputPayloadString = request.getInput();

        try {
            Result<InputPayloadImpl> resultPayload = parseInputPayload(inputPayloadString);

            // make InputPayloadMessage with a builder with stuff from InputPayloadImpl
            InputPayloadMessage message = getMessageFromPayload(resultPayload.getData());

            Any anyMessage = Any.pack(message);

            // Proto Result
            org.fibsters.ResultOuterClass.Result result = org.fibsters.ResultOuterClass.Result.newBuilder().setData(anyMessage).setSuccess(true).build();

            response = InputPayloadResponse.newBuilder().setResult(result).setMessage(message).build();
        } catch (Exception e) {
            org.fibsters.ResultOuterClass.Result result = org.fibsters.ResultOuterClass.Result.newBuilder().setSuccess(false).setErrorMessage("Error lol").build();

            e.printStackTrace();

            response = InputPayloadResponse.newBuilder().setResult(result).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

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