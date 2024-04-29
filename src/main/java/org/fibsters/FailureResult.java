package org.fibsters;

import org.fibsters.interfaces.Result;

public class FailureResult<T> implements Result {

    private T data;
    private final boolean success;
    private final String errorMessage;

    public FailureResult(T data, String errorMessage) {
        // TODO: maybe log to file or something
        this.data = data;
        this.errorMessage = errorMessage;
        this.success = false;
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
        return this.errorMessage;
    }

}
