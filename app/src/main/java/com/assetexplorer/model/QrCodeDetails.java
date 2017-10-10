package com.assetexplorer.model;

public class QrCodeDetails {
    private String associateId;
    private String associateName;
    private String qrCodeValue;
    private String time;

    public String toString() {
        return "QrCodeDetails{qrCodeValue='" + this.qrCodeValue + '\'' + ", associateId='" + this.associateId + '\'' + ", associateName='" + this.associateName + '\'' + ", time='" + this.time + '\'' + '}';
    }

    public QrCodeDetails(String qrCodeValue, String associateId, String associateName, String time) {
        this.qrCodeValue = qrCodeValue;
        this.associateId = associateId;
        this.associateName = associateName;
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQrCodeValue() {
        return this.qrCodeValue;
    }

    public void setQrCodeValue(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }

    public String getAssociateId() {
        return this.associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public String getAssociateName() {
        return this.associateName;
    }

    public void setAssociateName(String associateName) {
        this.associateName = associateName;
    }
}
