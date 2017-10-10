package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiDetailsPojo {
    @SerializedName("response")
    @Expose
    private ResponsePojo response;
    @SerializedName("version")
    @Expose
    private String version;

    public ResponsePojo getResponse() {
        return this.response;
    }

    public void setResponse(ResponsePojo response) {
        this.response = response;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return "ApiDetailsPojo{response=" + this.response + ", version='" + this.version + '\'' + '}';
    }
}
