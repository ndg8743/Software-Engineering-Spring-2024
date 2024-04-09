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
/*
import com.google.protobuf.Any;
        import io.grpc.stub.StreamObserver;

public class YourServiceImplementation extends YourServiceGrpc.YourServiceImplBase {

    @Override
    public void parseInputPayload(InputPayloadRequestOuterClass.InputPayloadRequest request, StreamObserver<InputPayloadResponseOuterClass.InputPayloadResponse> responseObserver) {
        // Initialize response to avoid sending null in case of exception
        InputPayloadResponseOuterClass.InputPayloadResponse.Builder responseBuilder = InputPayloadResponseOuterClass.InputPayloadResponse.newBuilder();

        String inputPayloadString = request.getInput();

        try {
            // Assume parseInputPayload returns a Result<InputPayloadImpl> object
            Result<InputPayloadImpl> resultPayload = parseInputPayload(inputPayloadString);

            if (resultPayload.isSuccess()) {
                // Assuming InputPayloadImpl has getDelimiter method
                String delimiter = resultPayload.getData().getDelimiter();

                // If InputPayloadMessage contains more fields, set them accordingly
                InputPayloadMessageOuterClass.InputPayloadMessage message = InputPayloadMessageOuterClass.InputPayloadMessage.newBuilder()
                        .setDelimiter(delimiter)
                        .build(); // Build the message after setting all fields

                // Wrapping the message in an Any type if Result data expects Any
                // Adjust according to your Result's .setData() method parameter type
                Any anyMessage = Any.pack(message);

                responseBuilder.setResult(
                        org.fibsters.ResultOuterClass.Result.newBuilder()
                                .setSuccess(true)
                                .setData(anyMessage) // Set the Protobuf message here
                                .build()
                );
            } else {
                // Handle failure case
                responseBuilder.setResult(
                        org.fibsters.ResultOuterClass.Result.newBuilder()
                                .setSuccess(false)
                                .setError("Error processing input payload") // Provide more specific error information if possible
                                .build()
                );
            }
        } catch (Exception e) {
            // Handle exception
            responseBuilder.setResult(
                    org.fibsters.ResultOuterClass.Result.newBuilder()
                            .setSuccess(false)
                            .setError("Exception occurred: " + e.getMessage())
                            .build()
            );
        }

        // Use the response builder to build the response object
        InputPayloadResponseOuterClass.InputPayloadResponse response = responseBuilder.build();

        // Sending the response to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Your existing parseInputPayload method seems mostly correct, assuming the implementations of SuccessResult, FailureResult, and InputPayloadImpl are appropriate and compatible with your schema.
}
*/