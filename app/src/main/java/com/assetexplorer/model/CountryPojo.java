package com.assetexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryPojo {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("currency")
    @Expose
    private Object currency;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dialcode")
    @Expose
    private Integer dialcode;
    @SerializedName("enabled")
    @Expose
    private Object enabled;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("localName")
    @Expose
    private String localName;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nationality")
    @Expose
    private Object nationality;
    @SerializedName("ordercoloumn")
    @Expose
    private Integer ordercoloumn;
    @SerializedName("version")
    @Expose
    private Integer version;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Object getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Object enabled) {
        this.enabled = enabled;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getCurrency() {
        return this.currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public Object getNationality() {
        return this.nationality;
    }

    public void setNationality(Object nationality) {
        this.nationality = nationality;
    }

    public String getLocalName() {
        return this.localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Integer getDialcode() {
        return this.dialcode;
    }

    public void setDialcode(Integer dialcode) {
        this.dialcode = dialcode;
    }

    public Integer getOrdercoloumn() {
        return this.ordercoloumn;
    }

    public void setOrdercoloumn(Integer ordercoloumn) {
        this.ordercoloumn = ordercoloumn;
    }
}
