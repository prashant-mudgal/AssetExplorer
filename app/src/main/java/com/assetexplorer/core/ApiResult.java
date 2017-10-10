package com.assetexplorer.core;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiResult {
    private Object data;
    private ApiError error;
    private String message;
    private Boolean success;

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public boolean dataIsArray() {
        return this.data != null && (this.data instanceof JSONArray);
    }

    public boolean dataIsObject() {
        return this.data != null && (this.data instanceof JSONObject);
    }

    public boolean dataIsInteger() {
        return this.data != null && (this.data instanceof Integer);
    }

    public JSONArray getDataAsArray() {
        if (dataIsArray()) {
            return (JSONArray) this.data;
        }
        return null;
    }

    public JSONObject getDataAsObject() {
        if (dataIsObject()) {
            return (JSONObject) this.data;
        }
        return null;
    }

    public Integer getDataAsInteger() {
        if (dataIsInteger()) {
            return (Integer) this.data;
        }
        return null;
    }

    public ApiError getError() {
        return this.error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
