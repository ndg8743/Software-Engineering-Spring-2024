import org.fibsters.ComputeJobStatus;
import org.fibsters.FibCalcComputeEngineImpl;
import org.fibsters.InputPayloadImpl;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FibCalcComputeEngineTest {

    @Test
    public void testFibCalcComputeEngine() {
        FibCalcComputeEngineImpl fibCalcCE = new FibCalcComputeEngineImpl();
        InputPayloadImpl inputPayload = Mockito.mock(InputPayloadImpl.class);

        fibCalcCE.setInputPayload(inputPayload);

        ComputeJobStatus status = fibCalcCE.getStatus();

        assert status == ComputeJobStatus.UNSTARTED;

        fibCalcCE.setStatus(ComputeJobStatus.PENDING);

        status = fibCalcCE.getStatus();

        assert status == ComputeJobStatus.PENDING;

        InputPayloadImpl inputPayload2;

        List<Integer> list = new ArrayList<>();

        Collections.addAll(list, 1, 10, 25);

        try {
            inputPayload2 = new InputPayloadImpl(InMemoryDatastore.getProperInputConfig(list)); // "{'CalcFibNumbersUpTo': [1, 10, 25]}"
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        fibCalcCE.setInputPayload(inputPayload2);

        // test uniqueID
        assert fibCalcCE.getInputPayload().getUniqueID().equals("1234");
        // test inputType
        assert fibCalcCE.getInputPayload().getInputType().equals("json");
        // test delimiter
        assert fibCalcCE.getInputPayload().getDelimiter().equals("");
        // test outputType
        assert fibCalcCE.getInputPayload().getOutputType().equals("json");
        // test outputSource
        assert fibCalcCE.getInputPayload().getOutputSource().equals("output.json");

        fibCalcCE.run(); // not starting in a thread so it should be synchronous

        assert fibCalcCE.getStatus() == ComputeJobStatus.COMPLETED;

        List<List<Integer>> fibCalcResults = fibCalcCE.getOutputPayload().getFibCalcResultsInteger2dList();

        assert fibCalcResults.size() == 3;
        assert fibCalcResults.get(0).size() == 1;
        assert fibCalcResults.get(1).size() == 10;
        assert fibCalcResults.get(2).size() == 25;

        assert fibCalcResults.get(2).get(24) == FibCalcComputeEngineImpl.calculateNthFibonacci(25); // 25th fibonacci number
    }

}
