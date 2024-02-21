import org.fibsters.CoordinatorComputeEngineImpl;
import org.fibsters.DataStorageImpl;
import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.interfaces.Result;
import org.fibsters.ComputeJobStatus;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CoordinatorComputeEngineTest {
    @Test
    public void testCompute() {
        System.out.println("CoordinatorComputeEngineTest");
        DataStorageImpl dataStorage = new DataStorageImpl();
        CoordinatorComputeEngineImpl computeAPI = new CoordinatorComputeEngineImpl(dataStorage);
        String inputString = InMemoryDatastore.getProperInputConfigString("{'CalcFibNumbersUpTo': [1, 10, 25]}");
        System.out.println(inputString);
        Result<InputPayload> result = computeAPI.parseInputPayload(inputString);
        assert result.isSuccess();
        assert result.getData().getInputType().equals("json");

        ComputeJob job = computeAPI.createComputeJobFromInputPayload(result.getData());

        assert job.getStatus() == ComputeJobStatus.UNSTARTED;
        computeAPI.queueJob(job);

        Instant startTime = Instant.now();
        Duration timeout = Duration.ofSeconds(10);

        while (job.getStatus().equals(ComputeJobStatus.UNSTARTED)) {
            if (Duration.between(startTime, Instant.now()).compareTo(timeout) > 0) {
                System.err.println("Timeout waiting for job to start");
                assert false; // timeout
                break;
            }
        }

        startTime = Instant.now();
        timeout = Duration.ofSeconds(10);

        while (!job.getStatus().equals(ComputeJobStatus.COMPLETED)) {
            if (Duration.between(startTime, Instant.now()).compareTo(timeout) > 0) {
                System.err.println("Timeout waiting for job to complete");
                assert false; // timeout
                break;
            }
        }
        OutputPayload output = job.getOutputPayload();
        assert output.isSuccess();
        List<int[]> fibCalcResults = output.getFibCalcResultsInteger2dList();
        assert fibCalcResults.size() == 3;
        assert fibCalcResults.get(0).length == 1;
        for (int[] fibCalcResult : fibCalcResults) {
            System.out.println(Arrays.toString(fibCalcResult));
        }


    }

    public void testComputeOld() {
        CoordinatorComputeEngineImpl computeAPI = Mockito.mock(CoordinatorComputeEngineImpl.class);
        String inputString = InMemoryDatastore.getProperInputConfigString("{'CalcFibNumbersUpTo': [1, 10, 25]}");
        System.out.println(inputString);
        Result<InputPayload> result = computeAPI.parseInputPayload(inputString);
        assert result.isSuccess();
        assert result.getData().getInputType().equals("json");

        ComputeJob job = computeAPI.createComputeJobFromInputPayload(result.getData());

        assert job.getStatus() == ComputeJobStatus.UNSTARTED;
        computeAPI.queueJob(job);
        assert job.getStatus() == ComputeJobStatus.PENDING;

        Instant startTime = Instant.now();
        Duration timeout = Duration.ofSeconds(5);

        while (computeAPI.getJobStatus(job) != ComputeJobStatus.COMPLETED) {
            if (Duration.between(startTime, Instant.now()).compareTo(timeout) > 0) {
                assert false; // timeout
                break;
            }
        }
        Result<OutputPayload> output = computeAPI.parseOutputPayload(job);
        assert output.isSuccess();
        List<int[]> fibCalcResults = output.getData().getFibCalcResultsInteger2dList();
        assert fibCalcResults.size() == 3;
        assert fibCalcResults.get(0).length == 1;
    }
}
