package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;

import com.ndrmf.complaint.model.Complaint;
import com.ndrmf.user.model.User;

public class AddComplaintReview {

	private LocalDateTime reviewAddDateTime;
	private String comments;
	private boolean isSatisfied;
	
	public AddComplaintReview(LocalDateTime reviewAddDateTime, String comments,boolean isSatisfied) {
		super();
		this.reviewAddDateTime = reviewAddDateTime;
		this.comments = comments;
		this.isSatisfied=isSatisfied;
	}
	
	public LocalDateTime getReviewAddDateTime() {
		return reviewAddDateTime;
	}
	public void setReviewAddDateTime(LocalDateTime reviewAddDateTime) {
		this.reviewAddDateTime = reviewAddDateTime;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isSatisfied() {
		return isSatisfied;
	}

	public void setSatisfied(boolean isSatisfied) {
		this.isSatisfied = isSatisfied;
	}
	
	
}
