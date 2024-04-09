package org.cliclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.fibsters.*;
import org.fibsters.InputPayloadRequestOuterClass;
import org.fibsters.InputPayloadResponseOuterClass;
import org.fibsters.InputPayloadServiceGrpc;
import org.fibsters.interfaces.PayloadData;
import org.fibsters.interfaces.Result;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Starting the compute job client...");

            while (true) {
                List<Integer> userInput = getUserInputArr();
                int[] userinputArr = userInput.stream().mapToInt(i -> i).toArray();

                String startJobJson = createStartJobFromInput(userinputArr);
                String jobId = startComputeJob(startJobJson);

                if (jobId != null) {
                    doJob(jobId);
                }
                System.out.println("Do you want to start another job? (yes/no)");
                String response = scanner.nextLine();
                if (!"yes".equalsIgnoreCase(response)) {
                    break;
                }
            }
        } else { //test data store from client
            System.out.println("Test datastore from client with grpc...");
            // Starting compute job with initial JSON
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();

            InputPayloadServiceGrpc.InputPayloadServiceBlockingStub stub = InputPayloadServiceGrpc.newBlockingStub(channel);
            String jsontest = "{\"payloadData\":{\"calcFibNumbersUpTo\":[1,10,25,70]},\"directive\":\"SUBMIT_COMPUTE_JOB\"}";
            InputPayloadRequestOuterClass.InputPayloadRequest request = InputPayloadRequestOuterClass.InputPayloadRequest.newBuilder().setInput(jsontest).build();

            InputPayloadResponseOuterClass.InputPayloadResponse response = stub.parseMessage(request);

            System.out.println("Message: " + response.getMessage() + " \t Result:" + response.getResult());

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
                    System.out.println("Job result: " + result);
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

            // save the buffered image to a file
            if (outputPayload.getOutputImage() != null) {
                saveImageToFile(outputPayload);
            }

        }
    }

    private static void saveImageToFile(OutputPayloadImpl outputPayload) {
        BufferedImage image = outputPayload.getOutputImage();
        if (image != null) {
            System.out.println("Saving the image to a file...");
            try {
                ImageIO.write(image, "png", new java.io.File("fibonacci.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        String response = sendPostRequest(json);
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
        String response = sendPostRequest(json);
        if (response != null) {
            Type resultType = new TypeToken<SuccessResult<OutputPayloadImpl>>() {
            }.getType();
            return gson_noBuff.fromJson(response, resultType);
        }
        return null;
    }
    private static ComputeJobStatus checkJobStatus(String json) {
        // Send the status check request and return the job status
        String response = sendPostRequest(json);
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
