package com.ndrmf.engine.dto;
import java.util.UUID;

public class InitialAdvanceSubmitReviewRequest {
    private UUID id;
    private String comments;
    private String subStatus;
    private UUID qaId;
    private String type;

    public UUID getQaId() {
        return qaId;
    }

    public void setQaId(UUID qaId) {
        this.qaId = qaId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }
}
