import org.json.JSONObject;

public class TestHelpers {

    public static String getProperInputConfigString(String customPayloadData) {
        JSONObject properInputPayloadJSON = getProperInputConfig(customPayloadData);
        return properInputPayloadJSON.toString();
    }
    public static JSONObject getProperInputConfig(String customPayloadData) {
        JSONObject properInputPayloadJSON = new JSONObject();
        properInputPayloadJSON.put("uniqueID", "1234");
        properInputPayloadJSON.put("inputType", "json");
        properInputPayloadJSON.put("payloadData", customPayloadData != null ? customPayloadData : "{'CalcFibNumbersUpTo': [6, 7, 8]}");
        properInputPayloadJSON.put("delimiter", "");
        properInputPayloadJSON.put("outputType", "json");
        properInputPayloadJSON.put("outputSource", "output.json");
        return properInputPayloadJSON;
    }
}
