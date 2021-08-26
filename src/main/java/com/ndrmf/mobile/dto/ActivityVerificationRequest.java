package com.ndrmf.mobile.dto;
import java.util.UUID;

public class ActivityVerificationRequest {

    private int quarter;
    private String activityId;
    private UUID proposalId;
    private String comments;
    private int rating;
    private String status;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public UUID getProposalId() {
        return proposalId;
    }

    public void setProposalId(UUID proposalId) {
        this.proposalId = proposalId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
