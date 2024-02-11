import org.fibsters.ComputeJobStatus;
import org.fibsters.CoordinatorComputeEngineImpl;
import org.fibsters.FibCalcComputeEngineImpl;
import org.fibsters.InputPayloadImpl;
import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.CoordinatorComputeEngine;
import org.fibsters.interfaces.InputPayload;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

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
        try {
            inputPayload2 = new InputPayloadImpl(TestHelpers.getProperInputPayload());
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
    }
}
