package org.fibsters;
import org.fibsters.interfaces.Result;
import org.json.JSONObject;

public class SuccessResult<T> implements Result {

    private T data;
    private Boolean success = true;

    public SuccessResult(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return success;
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

}
