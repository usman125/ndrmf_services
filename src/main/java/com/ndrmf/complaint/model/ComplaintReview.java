package com.ndrmf.complaint.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.ndrmf.user.model.User;

@Entity
@Table(name="complaint_review")
public class ComplaintReview {

	private UUID id;
	private Complaint complaintRef;
	private User userRef;
	private LocalDateTime dateTime;
	private String comments;
	private boolean isSatisfied;
	
	public ComplaintReview() {}

	public ComplaintReview(UUID id, Complaint complaintRef, User userRef, LocalDateTime dateTime, String remarks,boolean isSatisfied) {
		super();
		this.id = id;
		this.complaintRef = complaintRef;
		this.userRef = userRef;
		this.dateTime = dateTime;
		this.comments = remarks;
		this.isSatisfied=isSatisfied;
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
	@JoinColumn(name="complaint_id",columnDefinition = "uuid")
	public Complaint getComplaintRef() {
		return complaintRef;
	}

	public void setComplaintRef(Complaint complaintRef) {
		this.complaintRef = complaintRef;
	}

	@ManyToOne
	@JoinColumn(name="user_id",columnDefinition = "uuid")
	public User getUserRef() {
		return userRef;
	}

	public void setUserRef(User userRef) {
		this.userRef = userRef;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String remarks) {
		this.comments = remarks;
	}

	public boolean isSatisfied() {
		return isSatisfied;
	}

	public void setSatisfied(boolean isSatisfied) {
		this.isSatisfied = isSatisfied;
	}
	
	
}
