import org.fibsters.*;
import org.fibsters.interfaces.*;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*; // import all static methods from Mockito

// Mockito Smoke Tests
public class SmokeTests {
    @Test
    public void testFibCalcComputeEngine() {
        FibCalcComputeEngineImpl fibCalcCE = Mockito.mock(FibCalcComputeEngineImpl.class);
        // new inputPayload
        InputPayload inputPayload = Mockito.mock(InputPayload.class);
        // set inputPayload
        fibCalcCE.setInputPayload(inputPayload);
        // get inputPayload
        InputPayload returnPayload = fibCalcCE.getInputPayload();
        // run
        fibCalcCE.run();

    }

}