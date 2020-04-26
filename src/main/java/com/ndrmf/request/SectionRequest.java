package com.ndrmf.request;

public class SectionRequest extends ServiceRequest{
    private String sectionName;
    private String sectionKey;
    private String userName;
    private boolean formGenerated;
    private String formIdentity;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isFormGenerated() {
        return formGenerated;
    }

    public void setFormGenerated(boolean formGenerated) {
        this.formGenerated = formGenerated;
    }

    public String getFormIdentity() {
        return formIdentity;
    }

    public void setFormIdentity(String formIdentity) {
        this.formIdentity = formIdentity;
    }
}
