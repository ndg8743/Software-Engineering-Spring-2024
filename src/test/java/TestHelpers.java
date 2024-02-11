import org.json.JSONObject;

public class TestHelpers {

    public static String getProperInputConfigString(String CustomPayloadData) {
        JSONObject ProperInputPayloadJSON = getProperInputConfig(CustomPayloadData);
        return ProperInputPayloadJSON.toString();
    }
    public static JSONObject getProperInputConfig(String CustomPayloadData) {
        JSONObject ProperInputPayloadJSON = new JSONObject();
        ProperInputPayloadJSON.put("uniqueID", "1234");
        ProperInputPayloadJSON.put("inputType", "json");
        ProperInputPayloadJSON.put("payloadData", CustomPayloadData != null ? CustomPayloadData : "{'CalcFibNumbersUpTo': [6, 7, 8]}");
        ProperInputPayloadJSON.put("delimiter", "");
        ProperInputPayloadJSON.put("outputType", "json");
        ProperInputPayloadJSON.put("outputSource", "output.json");
        return ProperInputPayloadJSON;
    }
}
