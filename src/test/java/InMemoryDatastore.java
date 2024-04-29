import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.fibsters.BufferedImageTypeAdapter;
import org.fibsters.InputPayloadImpl;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class InMemoryDatastore {

    public static String getProperInputConfigString(String customPayloadData) {
        JSONObject properInputPayloadJSON = getProperInputConfig(customPayloadData);

        return properInputPayloadJSON.toString();
    }

    public static String getProperInputConfigString(String directive, String customPayloadData) {
        JSONObject properInputPayloadJSON = getProperInputConfig(directive, customPayloadData);

        return properInputPayloadJSON.toString();
    }

    public static JSONObject getProperInputConfig(String directive, String customPayloadData) {
        JSONObject properInputPayloadJSON = getProperInputConfig(customPayloadData);

        properInputPayloadJSON.put("directive", directive);

        return properInputPayloadJSON;
    }

    public static JSONObject getProperInputConfig(String customPayloadData) {
        JSONObject properInputPayloadJSON = new JSONObject();

        properInputPayloadJSON.put("uniqueID", "1234");
        properInputPayloadJSON.put("inputType", "json");

        JSONObject payloadData = !customPayloadData.isEmpty() ? new JSONObject(customPayloadData) : new JSONObject("{'calcFibNumbersUpTo': [6, 7, 8]}");

        properInputPayloadJSON.put("payloadData", payloadData);

        properInputPayloadJSON.put("delimiter", "");
        properInputPayloadJSON.put("outputType", "json");
        properInputPayloadJSON.put("outputSource", "output.json");

        return properInputPayloadJSON;
    }

    public static JSONObject getProperInputConfig(List<Integer> inputs) {
        String formattedArray = "{'calcFibNumbersUpTo': " + Arrays.toString(inputs.toArray()) + "}";

        return getProperInputConfig(formattedArray);
    }

    public static JSONObject getProperInputConfig(List<Integer> inputs, List<String> outputLocations) {
        String formattedArray = "{'calcFibNumbersUpTo': " + Arrays.toString(inputs.toArray()) +
                ",'outputLocations': " + Arrays.toString(outputLocations.toArray()) +
                "}";

        return getProperInputConfig(formattedArray);
    }

    private static final Gson gson_noBuff = new GsonBuilder()
            .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter(BufferedImageTypeAdapter.ImageType.NULL))
            .create();

    private static final Gson gson_buff = new GsonBuilder()
            .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter())
            .create();

    public static InputPayloadImpl convertJSONObjToInputPayload(String jsonString) throws JsonSyntaxException {
        return gson_noBuff.fromJson(jsonString, InputPayloadImpl.class);
    }

    public static InputPayloadImpl convertJSONObjToInputPayload(JSONObject json) throws JsonSyntaxException {
        String jsonString = json.toString();
        return convertJSONObjToInputPayload(jsonString);
    }

}
