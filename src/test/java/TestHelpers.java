import org.json.JSONObject;

public class TestHelpers {
    public static JSONObject getProperInputPayload() {
        JSONObject ProperInputPayloadJSON = new JSONObject();
        ProperInputPayloadJSON.put("uniqueID", "1234");
        ProperInputPayloadJSON.put("inputType", "json");
        ProperInputPayloadJSON.put("payloadData", "{'input': '5'}");
        ProperInputPayloadJSON.put("delimiter", "");
        ProperInputPayloadJSON.put("outputType", "json");
        ProperInputPayloadJSON.put("outputSource", "output.json");
        return ProperInputPayloadJSON;
    }
}
