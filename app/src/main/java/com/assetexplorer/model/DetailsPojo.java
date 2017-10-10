package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsPojo {
    @SerializedName("field-names")
    @Expose
    private FieldNamesPojo fieldNames;
    @SerializedName("field-values")
    @Expose
    private FieldValuesPojo fieldValues;

    public FieldNamesPojo getFieldNames() {
        return this.fieldNames;
    }

    public void setFieldNames(FieldNamesPojo fieldNames) {
        this.fieldNames = fieldNames;
    }

    public FieldValuesPojo getFieldValues() {
        return this.fieldValues;
    }

    public void setFieldValues(FieldValuesPojo fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String toString() {
        return "DetailsPojo{fieldNames=" + this.fieldNames + ", fieldValues=" + this.fieldValues + '}';
    }
}
