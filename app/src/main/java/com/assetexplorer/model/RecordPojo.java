package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecordPojo {
    @SerializedName("value")
    @Expose
    private List<String> value;

    public RecordPojo() {
        this.value = null;
    }

    public List<String> getValue() {
        return this.value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public String toString() {
        return "RecordPojo{value=" + this.value + '}';
    }
}
