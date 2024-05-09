package org.fibsters;

import org.fibsters.interfaces.Result;

public class SuccessResult<T> implements Result {

    private T data;
    private final boolean success;

    public SuccessResult(T data) {
        this.data = data;
        this.success = true;
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
