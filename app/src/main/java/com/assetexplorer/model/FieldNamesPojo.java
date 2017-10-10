package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FieldNamesPojo {
    @SerializedName("name")
    @Expose
    private List<NamePojo> name;

    public FieldNamesPojo() {
        this.name = null;
    }

    public List<NamePojo> getName() {
        return this.name;
    }

    public void setName(List<NamePojo> name) {
        this.name = name;
    }

    public String toString() {
        return "FieldNamesPojo{name=" + this.name + '}';
    }
}
