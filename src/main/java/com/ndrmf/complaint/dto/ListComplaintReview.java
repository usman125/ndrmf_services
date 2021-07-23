package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;
import java.util.UUID;


public class ListComplaintReview {

	private UUID id;
	private UUID complaintId;
	private UUID userId;
	private String userName;
	private String email;
	private String org;
	private LocalDateTime reviewDateTime;
	private String comments;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(UUID complaintId) {
		this.complaintId = complaintId;
	}
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public LocalDateTime getReviewDateTime() {
		return reviewDateTime;
	}
	public void setReviewDateTime(LocalDateTime reviewDateTime) {
		this.reviewDateTime = reviewDateTime;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	
	
	
}
