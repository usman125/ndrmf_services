package com.ndrmf.request;

import java.util.List;

public class ReviewCreateRequest extends ServiceRequest {

    //Following two can be used to fetch accreditation id
    private String username;
    private String sectionKey;
    private String status;
    private String comments;
    private int rating;
    private String sectionReviewer;
    private List<CompReviewCreateRequest> compReviewCreateRequestList;


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

    public List<CompReviewCreateRequest> getCompReviewCreateRequestList() {
        return compReviewCreateRequestList;
    }

    public void setCompReviewCreateRequestList(List<CompReviewCreateRequest> compReviewCreateRequestList) {
        this.compReviewCreateRequestList = compReviewCreateRequestList;
    }

    public String getSectionReviewer() {
        return sectionReviewer;
    }

    public void setSectionReviewer(String sectionReviewer) {
        this.sectionReviewer = sectionReviewer;
    }
}
