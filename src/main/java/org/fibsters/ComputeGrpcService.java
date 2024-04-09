package org.fibsters;

import com.google.protobuf.Any;
import io.grpc.stub.StreamObserver;
import org.fibsters.ComputeInputRequestOuterClass.ComputeInputRequest;
import org.fibsters.ComputeInputResponseOuterClass.ComputeInputResponse;
import org.fibsters.ComputeInputMessageOuterClass.ComputeInputMessage;
import org.fibsters.ComputeInputServiceGrpc.ComputeInputServiceImplBase;

public class ComputeGrpcService extends ComputeInputServiceImplBase {

    private CoordinatorComputeEngineImpl api;

    ComputeGrpcService(CoordinatorComputeEngineImpl api) {
        this.api = api;
    }

    @Override
    public void processInputStringForOutput(ComputeInputRequest request, StreamObserver<ComputeInputResponse> responseObserver) {
        ComputeInputResponse response;
        // get input from request
        String inputString = request.getInput();

        // send to processInputStringForOutput(String inputString) & get string back
        try {
            String resultString = processInputStringForOutput(inputString);

            // make InputPayloadMessage with a builder with stuff from InputPayloadImpl
            ComputeInputMessage message = ComputeInputMessage.newBuilder().setInput(resultString).build();

            Any anyMessage = Any.pack(message);

            // Proto Result
            org.fibsters.ResultOuterClass.Result result = org.fibsters.ResultOuterClass.Result.newBuilder().setData(anyMessage).setSuccess(true).build();

            response = ComputeInputResponse.newBuilder().setResult(result).setOutput(message).build();
        } catch (Exception e) {
            org.fibsters.ResultOuterClass.Result result = org.fibsters.ResultOuterClass.Result.newBuilder().setSuccess(false).setErrorMessage("Error lol processInputStringForOutput").build();

            e.printStackTrace();

            response = ComputeInputResponse.newBuilder().setResult(result).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String processInputStringForOutput(String inputString) {
        String response = this.api.processInputStringForOutput(inputString);
        return response;
    }
}
