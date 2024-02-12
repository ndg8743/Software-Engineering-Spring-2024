package org.fibsters.interfaces;

import org.json.JSONObject;

// Generic wrapper interface for the result of a computation.
// There will be a SuccessResult and a FailureResult class that implement this interface.
// i.e. public class SuccessResult<T> implements Result<T> { ... }
// Then in processPayload, we can return a new SuccessResult or FailureResult.
public interface Result<T> {
    boolean isSuccess();

    T getData();

    void setData(T data);

    String getErrorMessage();

    JSONObject toJSON();
}
