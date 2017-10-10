package com.assetexplorer.core;

import android.support.annotation.NonNull;
import com.assetexplorer.utility.AppUtils;

public class ApiError {
    private String message;
    private ApiErrorType type;

    public ApiError(String type, String message) {
        ApiErrorType errorType = (ApiErrorType) AppUtils.stringToEnum(ApiErrorType.class, type);
        if (errorType != null) {
            this.type = errorType;
        } else {
            this.type = ApiErrorType.UNKNOWN;
        }
        this.message = message;
    }

    public ApiError(@NonNull ApiErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ApiErrorType getType() {
        return this.type;
    }

    public void setType(ApiErrorType type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
