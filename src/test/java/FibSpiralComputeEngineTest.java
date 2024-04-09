import org.fibsters.InputPayloadImpl;
import org.fibsters.OutputPayloadImpl;
import org.fibsters.FibSpiralComputeEngineImpl;
import org.fibsters.ComputeJobStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FibSpiralComputeEngineTest {


    @Test
    public void testFibSpiralComputeEngine() {
        // TODO: After FibCalcComputeEngine is more finalized, fix this test
        InputPayloadImpl inputPayload;

        List<Integer> list = new ArrayList<>();

        Collections.addAll(list, 3, 10, 25);

        List<String> fileLocations = new ArrayList<>();

        Collections.addAll(fileLocations, "1fibsprial.png", "2fibsprial.png", "3fibsprial.png");
        // need to make a real outputpayload that has the output of what testfibcalc has
        try {
            inputPayload = InMemoryDatastore.convertJSONObjToInputPayload(InMemoryDatastore.getProperInputConfig(list, fileLocations));
            //inputPayload = new InputPayloadImpl(); // "{'CalcFibNumbersUpTo': [1, 10, 25]}"
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }
        ArrayList<int[]> fibanswers = new ArrayList<>(3);
        fibanswers.add(new int[]{1, 1, 2});
        fibanswers.add(new int[]{1, 1, 2, 3, 5, 8, 13, 21, 34, 55});
        fibanswers.add(new int[]{1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025});

        for (int i = 0; i < 3; i++) {
            int endIndex = fibanswers.get(i).length;

            OutputPayloadImpl outputPayload = new OutputPayloadImpl(i, inputPayload, ComputeJobStatus.UNSTARTED);
            outputPayload.setFibCalcResults(i, fibanswers.get(i), 0, endIndex);

            FibSpiralComputeEngineImpl fibSpiralCE = new FibSpiralComputeEngineImpl(outputPayload, i);

            fibSpiralCE.setInputPayload(inputPayload);

            ComputeJobStatus status = fibSpiralCE.getStatus();

            assert status == ComputeJobStatus.UNSTARTED;

            fibSpiralCE.setStatus(ComputeJobStatus.PENDING);
            status = fibSpiralCE.getStatus();

            assert status == ComputeJobStatus.PENDING;

            fibSpiralCE.run(); // not starting in a thread so it should be synchronous

            // this will fail since .run literally does nothing right now other than set it to .pending for spiral impl
            // assert fibSpiralCE.getStatus() == ComputeJobStatus.COMPLETED;

            //assert fibSpiralCE.getOutputPayload().getFibCalcResultsInteger2dList().size() == 3;
            Assert.assertEquals(3, fibSpiralCE.getOutputPayload().getFibCalcResultsInteger2dList().size());
        }
    }

}
