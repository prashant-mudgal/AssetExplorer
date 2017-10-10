package com.assetexplorer.model;

public class FieldNameValuePojo {
    private String fieldName;
    private String fieldType;
    private String fieldValue;

    public FieldNameValuePojo(String fieldName, String fieldValue, String fieldType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String toString() {
        return "FieldNameValuePojo{fieldName='" + this.fieldName + '\'' + ", fieldValue='" + this.fieldValue + '\'' + ", fieldType='" + this.fieldType + '\'' + '}';
    }
}
