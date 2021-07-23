package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;

public class ListComplainantReview {

	private String email;
	private LocalDateTime addedDateTime;
	private String feedback;
	private boolean isSatisfied;
	
	public ListComplainantReview() {}
	
	public ListComplainantReview(String email, LocalDateTime addedDateTime, String feedback,boolean isSatisfied) {
		super();
		this.email = email;
		this.addedDateTime = addedDateTime;
		this.feedback = feedback;
		this.isSatisfied=isSatisfied;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getAddedDateTime() {
		return addedDateTime;
	}
	public void setAddedDateTime(LocalDateTime addedDateTime) {
		this.addedDateTime = addedDateTime;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public boolean isSatisfied() {
		return isSatisfied;
	}

	public void setSatisfied(boolean isSatisfied) {
		this.isSatisfied = isSatisfied;
	}
	
	
	
}
