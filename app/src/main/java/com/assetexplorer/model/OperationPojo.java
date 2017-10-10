package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperationPojo {
    @SerializedName("Details")
    @Expose
    private DetailsPojo details;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("result")
    @Expose
    private ResultPojo result;

    public ResultPojo getResult() {
        return this.result;
    }

    public void setResult(ResultPojo result) {
        this.result = result;
    }

    public DetailsPojo getDetails() {
        return this.details;
    }

    public void setDetails(DetailsPojo details) {
        this.details = details;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "OperationPojo{result=" + this.result + ", details=" + this.details + ", name='" + this.name + '\'' + '}';
    }
}
