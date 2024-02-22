package org.fibsters;
import org.fibsters.interfaces.Result;
import org.json.JSONObject;

public class FailureResult<T> implements Result {

    private T data;
    private final String errorMessage;

    public FailureResult(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public void setData(Object data) {
        this.data = (T) data;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();

        result.put("success", this.isSuccess());
        result.put("data", this.data.toString());
        result.put("errorMessage", this.errorMessage);

        return result;
    }

}
