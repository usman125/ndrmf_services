package com.ndrmf.engine.dto;

import java.util.List;
import java.util.Date;
import java.util.UUID;

public class AddGrantImplementationAgreementRequest {
	private String data;
	private String poComments;
	private Date startDate;
	private Date endDate;
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

	public String getPoComments() {
		return poComments;
	}

	public void setPoComments(String poComments) {
		this.poComments = poComments;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
