import org.fibsters.ComputeJobStatus;
import org.fibsters.FibCalcComputeEngineImpl;
import org.fibsters.InputPayloadImpl;
import org.fibsters.OutputPayloadImpl;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FibCalcComputeEngineTest {

    @Test
    public void testFibCalcComputeEngine() {
        InputPayloadImpl inputPayload = Mockito.mock(InputPayloadImpl.class);
        OutputPayloadImpl outputPayload = Mockito.mock(OutputPayloadImpl.class);
        FibCalcComputeEngineImpl fibCalcCEMock = new FibCalcComputeEngineImpl(outputPayload);
        fibCalcCEMock.setInputPayload(inputPayload);

        ComputeJobStatus status = fibCalcCEMock.getStatus();

        assert status == ComputeJobStatus.UNSTARTED;

        fibCalcCEMock.setStatus(ComputeJobStatus.PENDING);
        status = fibCalcCEMock.getStatus();

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

        OutputPayloadImpl outputPayload2 = new OutputPayloadImpl(0, inputPayload2, ComputeJobStatus.UNSTARTED);
        FibCalcComputeEngineImpl fibCalcCE = new FibCalcComputeEngineImpl(outputPayload2);

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

        // simulating coordinator
        int[] getPayloadData = fibCalcCE.getInputPayload().getPayloadDataParsed();

        for (int i = 0; i < getPayloadData.length; i++) {
            fibCalcCE.setStartIndex(0);
            fibCalcCE.setEndIndex(getPayloadData[i]);
            fibCalcCE.setChunk(i);
            fibCalcCE.run();

            assert fibCalcCE.getStatus() == ComputeJobStatus.COMPLETED;
        }

        List<int[]> fibCalcResults = fibCalcCE.getOutputPayload().getFibCalcResultsInteger2dList();

        assert fibCalcResults.size() == 3;
        assert fibCalcResults.get(0).length == 1;
        assert fibCalcResults.get(1).length == 10;
        assert fibCalcResults.get(2).length == 25;

        for (int i = 0; i < fibCalcResults.get(2).length; i++) {
            assert fibCalcResults.get(2)[i] == FibCalcComputeEngineImpl.calculateNthFibonacci(i);
        }
    }

}
