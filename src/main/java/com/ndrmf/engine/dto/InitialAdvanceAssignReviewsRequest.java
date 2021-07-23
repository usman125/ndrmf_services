package com.ndrmf.engine.dto;

import java.util.List;
import java.util.UUID;

public class InitialAdvanceAssignReviewsRequest {
    public List<UUID> reviewers;
    public String comments;
    public UUID initialAdvanceId;

    public List<UUID> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<UUID> reviewers) {
        this.reviewers = reviewers;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public UUID getInitialAdvanceId() {
        return initialAdvanceId;
    }

    public void setInitialAdvanceId(UUID initialAdvanceId) {
        this.initialAdvanceId = initialAdvanceId;
    }
}
