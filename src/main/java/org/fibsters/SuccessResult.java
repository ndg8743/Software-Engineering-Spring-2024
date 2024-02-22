package org.fibsters;
import org.fibsters.interfaces.Result;
import org.json.JSONObject;

public class SuccessResult<T> implements Result {

    private T data;

    public SuccessResult(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return true;
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
        return null;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();

        result.put("success", this.isSuccess());
        result.put("data", this.data.toString());
        result.put("errorMessage", "");

        return result;
    }

}
