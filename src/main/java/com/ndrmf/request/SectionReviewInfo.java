package com.ndrmf.request;

import java.util.List;

public class SectionReviewInfo {

    private String username;
    private String sectionKey;
    private String status;
    private String comments;
    private int rating;
    private String reviewer;
    private List<ComponentReviewInfo> componentReviewInfos;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public List<ComponentReviewInfo> getComponentReviewInfos() {
        return componentReviewInfos;
    }

    public void setComponentReviewInfos(List<ComponentReviewInfo> componentReviewInfos) {
        this.componentReviewInfos = componentReviewInfos;
    }
}
