package com.ndrmf.engine.dto;

import java.util.Date;

public class OfferLetterUpdateRequest {

    private String fipComments;
    private String gmComments;
    private Date expiryDate;
    private String gmStatus;
    private String fipStatus;
    private String gmResponse;
    private String fipResponse;

    public String getFipComments() {
        return fipComments;
    }

    public void setFipComments(String fipComments) {
        this.fipComments = fipComments;
    }

    public String getGmComments() {
        return gmComments;
    }

    public void setGmComments(String gmComments) {
        this.gmComments = gmComments;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setGmStatus(String gmStatus) {
        this.gmStatus = gmStatus;
    }

    public String getGmStatus() {
        return gmStatus;
    }

    public void setFipStatus(String fipStatus) {
        this.fipStatus = fipStatus;
    }

    public String getFipStatus() {
        return fipStatus;
    }

    public void setGmResponse(String gmResponse) { this.gmResponse = gmResponse; }
    public String getGmResponse() { return gmResponse; }

    public void setFipResponse(String fipResponse) {
        this.fipResponse = fipResponse;
    }

    public String getFipResponse() {
        return fipResponse;
    }
}
