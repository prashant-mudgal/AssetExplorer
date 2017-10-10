package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldValuesPojo {
    @SerializedName("record")
    @Expose
    private RecordPojo record;
    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    public RecordPojo getRecord() {
        return this.record;
    }

    public void setRecord(RecordPojo record) {
        this.record = record;
    }

    public String getTotalRecords() {
        return this.totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String toString() {
        return "FieldValuesPojo{record=" + this.record + ", totalRecords='" + this.totalRecords + '\'' + '}';
    }
}
