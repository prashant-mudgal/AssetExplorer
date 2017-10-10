package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiPojo {
    @SerializedName("API")
    @Expose
    private ApiDetailsPojo aPI;

    public ApiDetailsPojo getAPI() {
        return this.aPI;
    }

    public void setAPI(ApiDetailsPojo aPI) {
        this.aPI = aPI;
    }
}
