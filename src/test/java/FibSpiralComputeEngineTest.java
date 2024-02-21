import org.fibsters.ComputeJobStatus;
import org.fibsters.FibCalcComputeEngineImpl;
import org.fibsters.InputPayloadImpl;
import org.fibsters.OutputPayloadImpl;
import org.junit.Test;
import org.mockito.Mockito;

public class FibSpiralComputeEngineTest {
    @Test
    public void testFibSpiralComputeEngine() {
        // TODO: After FibCalcComputeEngine is more finalized, fix this test

        FibCalcComputeEngineImpl fibSpiralCE = new FibCalcComputeEngineImpl(Mockito.mock(OutputPayloadImpl.class));
        InputPayloadImpl inputPayload = Mockito.mock(InputPayloadImpl.class);
        fibSpiralCE.setInputPayload(inputPayload);

        ComputeJobStatus status = fibSpiralCE.getStatus();
        assert status == ComputeJobStatus.UNSTARTED;

        fibSpiralCE.setStatus(ComputeJobStatus.PENDING);
        status = fibSpiralCE.getStatus();
        assert status == ComputeJobStatus.PENDING;

        OutputPayloadImpl outputPayload = Mockito.mock(OutputPayloadImpl.class);
        fibSpiralCE.setOutputPayload(outputPayload);

        fibSpiralCE.run(); // not starting in a thread so it should be synchronous
        assert fibSpiralCE.getStatus() == ComputeJobStatus.COMPLETED;

        assert fibSpiralCE.getOutputPayload().getFibCalcResultsInteger2dList().size() == 3;
    }
}
