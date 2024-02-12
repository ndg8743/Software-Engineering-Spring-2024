import org.fibsters.CoordinatorComputeEngineImpl;
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
import java.util.List;


public class CoordinatorComputeEngineTest {
    @Test
    public void testCompute() {
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
        List<List<Integer>> fibCalcResults = output.getData().getFibCalcResultsInteger2dList();
        assert fibCalcResults.size() == 3;
        assert fibCalcResults.get(0).size() == 1;
    }
}
