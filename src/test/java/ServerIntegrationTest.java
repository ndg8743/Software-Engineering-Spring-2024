import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.fibsters.Main;
import org.cliclient.CliClient;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerIntegrationTest {

    private static Thread serverThread;
    private static Process jconsoleProcess;
    private static int[] userinputArr = {6906, 34250, 14500};
    @BeforeAll
    public static void startServer() {
        serverThread = new Thread(() -> {
            String[] args = {"8080"};
            Main.main(args);
        });
        serverThread.start();  // Start the server thread
        System.out.println("[Test] Server started.");
        try {
            Thread.sleep(5000);  // Wait for the server to initialize completely
            // Launch JConsole and connect it to the local Java process
            String processId = Long.toString(ProcessHandle.current().pid()); // Assumes the current process is the one you want to monitor
            jconsoleProcess = new ProcessBuilder("jconsole", processId).start();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed to start JConsole");
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void stopServer() {
        if (serverThread != null) {
            serverThread.interrupt();  // Attempt to stop the server
            try {
                serverThread.join();  // Ensure the server thread has terminated
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[Test] Server stopped.");
        }
    }

    @Test
    void testSendPostRequestDuration() throws IOException {
        Instant start = Instant.now();

        Duration duration = Duration.between(start, Instant.now());
        testServerWithInput(userinputArr, "POST");
        System.out.println("Duration for sendPostRequest: " + duration.toMillis() + " ms");
        assertTrue(duration.toMillis() < 10000);
    }

    void testServerWithInput(int[] userinputArr, String requestType) {
        CliClient.setNetworkRequestType(requestType);
        String startJobJson = CliClient.createStartJobFromInput(userinputArr);
        String jobId = CliClient.startComputeJob(startJobJson);

        assertTrue(jobId != null);
        // send it off
        CliClient.doJob(jobId);
    }

    @Test
    void testSendGrpcRequestDuration() {
        Instant start = Instant.now();
        Duration duration = Duration.between(start, Instant.now());
        testServerWithInput(userinputArr, "POST");
        System.out.println("Duration for sendPostRequest: " + duration.toMillis() + " ms");
        assertTrue(duration.toMillis() < 10000);
    }
}
