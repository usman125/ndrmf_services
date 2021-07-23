package com.ndrmf.complaint.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ComplainantReview {

	private UUID id;
	private Complaint complaintRef;
	private String complainantName;
	private String email;
	private LocalDateTime addedDateTime;
	private String feedback;
	
	public ComplainantReview() {}
	
	public ComplainantReview(UUID id, Complaint complaintRef, String complainantName, String email,
			LocalDateTime addedDateTime,String feedback) {
		super();
		this.id = id;
		this.complaintRef = complaintRef;
		this.complainantName = complainantName;
		this.email = email;
		this.addedDateTime = addedDateTime;
		this.feedback= feedback;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="complaint_id")
	public Complaint getComplaintRef() {
		return complaintRef;
	}
	public void setComplaintRef(Complaint complaintRef) {
		this.complaintRef = complaintRef;
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
