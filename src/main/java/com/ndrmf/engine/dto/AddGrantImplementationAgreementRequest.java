package com.ndrmf.engine.dto;

import java.util.List;
import java.util.UUID;

public class AddGrantImplementationAgreementRequest {
	private String data;
	private List<UUID> reviewers;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<UUID> getReviewers() {
		return reviewers;
	}

	public void setReviewers(List<UUID> reviewers) {
		this.reviewers = reviewers;
	}
}
