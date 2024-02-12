import org.fibsters.OutputPayloadImpl;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class InMemoryDatastore {
    public static String getProperInputConfigString(String customPayloadData) {
        JSONObject properInputPayloadJSON = getProperInputConfig(customPayloadData);
        return properInputPayloadJSON.toString();
    }
    public static JSONObject getProperInputConfig(String customPayloadData) {
        JSONObject properInputPayloadJSON = new JSONObject();
        properInputPayloadJSON.put("uniqueID", "1234");
        properInputPayloadJSON.put("inputType", "json");
        properInputPayloadJSON.put("payloadData", customPayloadData.isEmpty() ? customPayloadData : "{'CalcFibNumbersUpTo': [6, 7, 8]}");
        properInputPayloadJSON.put("delimiter", "");
        properInputPayloadJSON.put("outputType", "json");
        properInputPayloadJSON.put("outputSource", "output.json");
        return properInputPayloadJSON;
    }

    public static JSONObject getProperInputConfig(List<Integer> inputs) {
        String formattedArray = "{'CalcFibNumbersUpTo': " + Arrays.toString(inputs.toArray());

        return getProperInputConfig(formattedArray);
    }

}
