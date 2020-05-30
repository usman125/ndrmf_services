package com.ndrmf.engine.dto;

public class AddProposalSectionReviewRequest {
	private String comments;
	private String status;

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
}
