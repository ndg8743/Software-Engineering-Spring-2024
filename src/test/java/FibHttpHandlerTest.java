import org.fibsters.*;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

// Mockito Smoke Tests
public class FibHttpHandlerTest {
    @Test
    public void testFibHttpHandler() throws IOException {
        /* ---- Test improper inputPayload Format ---- */
        JSONObject ImproperInputPayloadJSON = new JSONObject();
        ImproperInputPayloadJSON.put("input", "test");

        String inputString = ImproperInputPayloadJSON.toString();
        ByteArrayOutputStream responseStreamo =
                setupHttpHandlerMockito(
                        Mockito.mock(HttpExchange.class),
                        "POST",
                        inputString);
        String response = responseStreamo.toString();
        System.out.println(response);
        // verify response is a JSON object
        JSONObject responseJSON = verifyProperInputPayloadFormat(response);

        assert responseJSON.getBoolean("success") == false;

        /* ---- Test sending garbage string ---- */
        inputString = "test with invalid json";
        responseStreamo =
                setupHttpHandlerMockito(
                        Mockito.mock(HttpExchange.class),
                        "POST",
                        inputString);
        response = responseStreamo.toString();
        System.out.println(response);
        responseJSON = verifyProperInputPayloadFormat(response);

        assert responseJSON.getBoolean("success") == false;

        /* ---- Test proper inputPayload Format ---- */
        JSONObject ProperInputPayloadJSON = TestHelpers.getProperInputConfig(null);

        inputString = ProperInputPayloadJSON.toString();
        responseStreamo =
                setupHttpHandlerMockito(
                        Mockito.mock(HttpExchange.class),
                        "POST",
                        inputString);
        response = responseStreamo.toString();
        System.out.println(response);

        responseJSON = verifyProperInputPayloadFormat(response);

        assert responseJSON.getBoolean("success") == true;

    }

    // ---- Helper Functions ----
    private ByteArrayOutputStream setupHttpHandlerMockito(HttpExchange t, String requestMethod, String inputString) throws IOException {
        CoordinatorComputeEngineImpl api = new CoordinatorComputeEngineImpl(new DataStorageImpl());
        FibHttpHandler fibHttpHandler = new FibHttpHandler(api);

        when(t.getRequestBody()).thenReturn(Mockito.mock(InputStream.class));
        when(t.getRequestMethod()).thenReturn(requestMethod);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(t.getResponseBody()).thenReturn(responseStream);
        when(t.getRequestBody().readAllBytes()).thenReturn(inputString.getBytes());
        fibHttpHandler.handle(t);
        return responseStream;
    }

    private JSONObject verifyProperInputPayloadFormat(String response) {
        JSONObject responseJSON;
        try {
            responseJSON = new JSONObject(response);
        } catch (Exception e) {
            assert false;
            return null;
        }

        assert responseJSON.has("success");
        assert responseJSON.has("data");
        assert responseJSON.has("errorMessage");
        // I could simplify these to not have == true but it helps increase readability.
        assert responseJSON.getBoolean("success") == true || responseJSON.getBoolean("success") == false;
        if(responseJSON.getBoolean("success") == true) {
            assert responseJSON.getString("errorMessage").length() == 0;
        }
        else {
            assert responseJSON.getString("errorMessage").length() > 0;
        }
        return responseJSON;
    }
}