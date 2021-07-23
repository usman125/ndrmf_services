package com.ndrmf.engine.dto;

import java.util.UUID;

public class AddQprTaskReviewRequest {

	private String comments;
	private String decision;
	private UUID qprId;

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public UUID getQprId() {
		return qprId;
	}

	public void setQprId(UUID qprId) {
		this.qprId = qprId;
	}
}
