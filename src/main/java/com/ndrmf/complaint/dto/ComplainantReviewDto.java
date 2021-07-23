package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ComplainantReviewDto {

	private UUID complaintId;
	private String complainantName;
	private String email;
	private LocalDateTime addedDateTime;
	private String feedback;
	
	public ComplainantReviewDto(UUID complaintId, String complainantName, String email,LocalDateTime addedDateTime,String feedback) {
		super();
		this.complaintId = complaintId;
		this.complainantName = complainantName;
		this.email = email;
		this.addedDateTime = addedDateTime;
		this.feedback =  feedback;
	}
	public UUID getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(UUID complaintRef) {
		this.complaintId = complaintRef;
	}
	public String getComplainantName() {
		return complainantName;
	}
	public void setComplainantName(String complainantName) {
		this.complainantName = complainantName;
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
	
	
}
