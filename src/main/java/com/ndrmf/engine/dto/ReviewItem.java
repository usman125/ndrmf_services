package com.ndrmf.engine.dto;

import java.util.Date;

public class ReviewItem {
	private String controlWiseComments;
	private String rating;
	private String status;
	private String comments;
	private Date createdDate;
	
	public String getControlWiseComments() {
		return controlWiseComments;
	}
	public void setControlWiseComments(String controlWiseComments) {
		this.controlWiseComments = controlWiseComments;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
