package com.ndrmf.request;

public class FormUpdationRequest extends ServiceRequest {

    private String sectionKey;
    private String status;

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
