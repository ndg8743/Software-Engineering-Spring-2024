package org.cliclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.fibsters.*;
import org.fibsters.ComputeInputMessageOuterClass;
import org.fibsters.ComputeInputRequestOuterClass;
import org.fibsters.ComputeInputResponseOuterClass;
import org.fibsters.InputPayloadMessageOuterClass;
import org.fibsters.InputPayloadRequestOuterClass;
import org.fibsters.InputPayloadResponseOuterClass;
import org.fibsters.InputPayloadServiceGrpc;
import org.fibsters.interfaces.PayloadData;
import org.fibsters.interfaces.Result;
import org.fibsters.ComputeInputServiceGrpc;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CliClient {
    private static final Gson gson_noBuff = new GsonBuilder()
            .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter(BufferedImageTypeAdapter.ImageType.NULL))
            .create();
    private static final Gson gson_buff = new GsonBuilder()
            .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter())
            .create();

    private static final Scanner scanner = new Scanner(System.in);

    private static String networkRequestType = "POST";

    // TODO: this is very hacky, properly handle this later!
    private static String userSpecifiedFileName = "fibbonacci";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Starting the compute job client...");

            System.out.println("Set Network Request Type (POST/GRPC), POST by default");
            String response = scanner.nextLine();
            if ("GRPC".equalsIgnoreCase(response)) {
                CliClient.networkRequestType = "GRPC";
            }

            System.out.println("Set file name (Don't specify file type!): ");

            String inputFileName = scanner.nextLine();

            if (!inputFileName.isBlank() || !inputFileName.isEmpty()) {
                userSpecifiedFileName = inputFileName;
            }

            while (true) {
                List<Integer> userInput = getUserInputArr();
                int[] userinputArr = userInput.stream().mapToInt(i -> i).toArray();

                String startJobJson = createStartJobFromInput(userinputArr);
                String jobId = startComputeJob(startJobJson);

                if (jobId != null) {
                    doJob(jobId);
                }
                System.out.println("Do you want to start another job? (yes/no)");
                response = scanner.nextLine();
                if (!"yes".equalsIgnoreCase(response)) {
                    break;
                }
            }
        } else if (args[0].equalsIgnoreCase("test1")) { //test data store from client
            System.out.println("[Fib] (Test1) - Test datastore from client with grpc...");
            // Starting compute job with initial JSON
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();

            InputPayloadServiceGrpc.InputPayloadServiceBlockingStub stub = InputPayloadServiceGrpc.newBlockingStub(channel);
            String jsontest = "{\"payloadData\":{\"calcFibNumbersUpTo\":[1,10,25,70]},\"directive\":\"SUBMIT_COMPUTE_JOB\"}";
            InputPayloadRequestOuterClass.InputPayloadRequest request = InputPayloadRequestOuterClass.InputPayloadRequest.newBuilder().setInput(jsontest).build();

            InputPayloadResponseOuterClass.InputPayloadResponse response = stub.parseMessage(request);

            try {
                InputPayloadMessageOuterClass.InputPayloadMessage inputPayload = response.getResult().getData().unpack(InputPayloadMessageOuterClass.InputPayloadMessage.class);
                System.out.println("Message: " + response.getMessage() + " \t CalcFibNumbers:" + inputPayload.getPayloadData().getCalcFibNumbersUpToList().toString());
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }

            channel.shutdown();
        } else if (args[0].equalsIgnoreCase("test2")) { //test data store from client
            System.out.println("[Fib] (Test2) - Test compute engine from client with grpc...");
            // Starting compute job with initial JSON
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();

            ComputeInputServiceGrpc.ComputeInputServiceBlockingStub stub = ComputeInputServiceGrpc.newBlockingStub(channel);
            String jsontest = "{\"payloadData\":{\"calcFibNumbersUpTo\":[1,10,25,70]},\"directive\":\"SUBMIT_COMPUTE_JOB\"}";
            ComputeInputRequestOuterClass.ComputeInputRequest request = ComputeInputRequestOuterClass.ComputeInputRequest.newBuilder().setInput(jsontest).build();

            ComputeInputResponseOuterClass.ComputeInputResponse response = stub.processInputStringForOutput(request);

            try {
                ComputeInputMessageOuterClass.ComputeInputMessage responseMessage = response.getResult().getData().unpack(ComputeInputMessageOuterClass.ComputeInputMessage.class);
                System.out.println("Message from server: " + response.getResult().getErrorMessage() + " \t Response from server:" + responseMessage.getInput());
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }

            channel.shutdown();
        }
    }

    private static void doJob(String jobId) {
        boolean completed = false;
        while (!completed) {
            System.out.println("Checking job status for ID: " + jobId);

            PayloadWrapper payloadWrapper = new PayloadWrapper(jobId, InputPayloadImpl.DirectiveType.GET_JOB_STATUS_BY_ID, null);
            String statusJson = gson_noBuff.toJson(payloadWrapper);
            ComputeJobStatus status = checkJobStatus(statusJson);
            switch (status) {
                case UNSTARTED:
                    System.out.println("Job is unstarted. Waiting for a few seconds before checking again...");
                    wait(1000);
                    break;

                case PENDING:
                    System.out.println("Job is pending. Waiting for a few seconds before checking again...");
                    wait(1000);
                    break;

                case COMPLETED:
                    completed = true;
                    System.out.println("Job completed. Fetching results...");
                    //String resultJson = "{ \"uniqueID\": \"" + jobId + "\", \"directive\": \"GET_JOB_BY_ID\" }";
                    Result<OutputPayloadImpl> result = getJobById(jobId);
                    handleCompletedResults(result);
                    System.out.println("Job result: " + result.getData().getStatus().name());
                    break;
                default:
                    System.out.println("Job not found. Waiting for a few seconds before checking again...");
                    wait(1000);
                    break;
            }

        }
    }

    private static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handleCompletedResults(Result<OutputPayloadImpl> result) {
        if (result != null) {
            OutputPayloadImpl outputPayload = result.getData();

            // Print FibCalcResults
            List<int[]> fibCalcResults = outputPayload.getFibCalcResultsInteger2dList();

            for (int[] fibCalcResult : fibCalcResults) {
                for (int i : fibCalcResult) {
                    System.out.print(i + " ");
                }

                System.out.println();
            }

            saveImage(outputPayload.getOutputImage(), "fib_client.png");
        }
    }

    public static void saveImage(BufferedImage image, String fileName) {
        try {
            ImageIO.write(image, "png", new File(fileName));

            System.out.println("Fibonacci fractal image saved as " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> getUserInputArr() {
        List<Integer> userInput = new ArrayList<>();
        System.out.println("Enter numbers (enter ';' to finish):");
        while (true) {
            String input = scanner.nextLine();
            if (";".equalsIgnoreCase(input)) {
                break;
            }
            try {
                int number = Integer.parseInt(input);
                userInput.add(number);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or ';'.");
            }
        }
        return userInput;
    }

    public static String createStartJobFromInput(int[] userinput) {
        // Create an instance of PayloadData
        PayloadData payloadData = new PayloadDataImpl();
        payloadData.setCalcFibNumbersUpTo(userinput);
        PayloadWrapper payloadWrapper = new PayloadWrapper(null, InputPayloadImpl.DirectiveType.SUBMIT_COMPUTE_JOB, payloadData);

        // Convert the PayloadData object to a JSON string
        String startJobJson = gson_noBuff.toJson(payloadWrapper);
        return startJobJson;
    }

    private static String startComputeJob(String json) {
        String response = sendNetworkRequest(json); //sendNetworkRequest(networkRequestType, json);
        if (response != null) {
            Type resultType = new TypeToken<SuccessResult<OutputPayloadImpl>>() {
            }.getType();
            SuccessResult<OutputPayloadImpl> status = gson_noBuff.fromJson(response, resultType);
            return status.getData().getUniqueID();
        }
        return null;
    }

    private static Result<OutputPayloadImpl> getJobById(String id) {
        PayloadWrapper payloadWrapper = new PayloadWrapper(id, InputPayloadImpl.DirectiveType.GET_JOB_BY_ID, null);
        String json = gson_noBuff.toJson(payloadWrapper);
        String response = sendNetworkRequest(json);
        if (response != null) {
            Type resultType = new TypeToken<SuccessResult<OutputPayloadImpl>>() {
            }.getType();
            return gson_noBuff.fromJson(response, resultType);
        }
        return null;
    }
    private static ComputeJobStatus checkJobStatus(String json) {
        // Send the status check request and return the job status
        String response = sendNetworkRequest(json);
        if (response != null) {
            Type resultType = new TypeToken<SuccessResult<OutputPayloadImpl>>() {}.getType();
            Result<OutputPayloadImpl> status = gson_noBuff.fromJson(response, resultType);
            return status.getData().getStatus();
        }
        return null;
    }

    private static String fetchJobResult(String json) {
        // Fetch and return the job result
        return sendPostRequest(json);
    }

    private static String sendNetworkRequest(String json) {
        return switch (networkRequestType) { // insane lam
            case "GRPC" -> sendGrpcRequest(json);
            case "POST" -> sendPostRequest(json);
            default -> null;
        };
    }

    private static String sendGrpcRequest(String json) {
        //System.out.println("[Fib] (Test2) - Test compute engine from client with grpc...");
        // Starting compute job with initial JSON
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();

        ComputeInputServiceGrpc.ComputeInputServiceBlockingStub stub = ComputeInputServiceGrpc.newBlockingStub(channel);

        ComputeInputRequestOuterClass.ComputeInputRequest request = ComputeInputRequestOuterClass.ComputeInputRequest.newBuilder().setInput(json).build();

        ComputeInputResponseOuterClass.ComputeInputResponse response = stub.processInputStringForOutput(request);
        String jsonResponse;
        try {
            ComputeInputMessageOuterClass.ComputeInputMessage responseMessage = response.getResult().getData().unpack(ComputeInputMessageOuterClass.ComputeInputMessage.class);
            //System.out.println("Message from server: " + response.getResult().getErrorMessage() + " \t Response from server:" + responseMessage.getInput());
            jsonResponse = responseMessage.getInput();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

        channel.shutdown();
        return jsonResponse;
    }

    private static String sendPostRequest(String json) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:8080/fib");
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
