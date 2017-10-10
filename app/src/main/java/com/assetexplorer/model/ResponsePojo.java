package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePojo {
    @SerializedName("operation")
    @Expose
    private OperationPojo operation;

    public OperationPojo getOperation() {
        return this.operation;
    }

    public void setOperation(OperationPojo operation) {
        this.operation = operation;
    }

    public String toString() {
        return "ResponsePojo{operation=" + this.operation + '}';
    }
}
