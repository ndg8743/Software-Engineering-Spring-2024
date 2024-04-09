package org.fibsters.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.fibsters.BufferedImageTypeAdapter;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

/**
 * Generic wrapper interface for the result of a computation.
 * There will be a SuccessResult and a FailureResult class that implement this interface.
 * i.e. public class SuccessResult<T> implements Result<T> { ... }
 * Then in processPayload, we can return a new SuccessResult or FailureResult.
 */
public interface Result<T> {

    /**
     * Checks if the result is successful.
     * @return true if the result is successful, false otherwise.
     */
    boolean isSuccess();

    /**
     * Retrieves the data of the result.
     * @return the data of the result.
     */
    T getData();

    /**
     * Sets the data of the result.
     * @param data the data to be set.
     */
    void setData(T data);

    /**
     * Retrieves the error message of the result.
     * @return the error message of the result.
     */
    String getErrorMessage();

    /**
     * Converts the result to a JSONObject.
     * @return a JSONObject representation of the result.
     */
    default JSONObject toJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return new JSONObject(json);
    }

    /**
     * Converts the result to a JSON string.
     * @return a JSON string representation of the result.
     */
    default String toJSONString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter())
                .create();
        return gson.toJson(this);
    }

    /**
     * Converts the result to a shallow JSON string.
     * This method excludes fields with the TRANSIENT modifier.
     * Purpose: Exclude BufferedImage fibSpiralResult from the JSON string.
     * @return a shallow JSON string representation of the result.
     */
    default String toJSONStringShallow() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter(BufferedImageTypeAdapter.ImageType.NULL))
                .create();
        return gson.toJson(this);
    }

}