package com.ndrmf.request;

public class AccredSectionReviewRequest extends ServiceRequest{

    private String username;
    private String sectionKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }
}
