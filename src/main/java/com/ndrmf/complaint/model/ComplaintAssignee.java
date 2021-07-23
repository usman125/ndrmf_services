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
@Table(name="complaint_assignee")
public class ComplaintAssignee {

	private UUID id;
	private User assignedPerson;
	private Complaint complaintRef;
	private LocalDateTime assignedDateTime;
	
	public ComplaintAssignee() {}
	
	public ComplaintAssignee(UUID id, User assignedToPerson, Complaint complaint, LocalDateTime assignedDateTime) {
		super();
		this.id = id;
		this.assignedPerson = assignedToPerson;
		this.complaintRef = complaint;
		this.assignedDateTime = assignedDateTime;
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
	@JoinColumn(name="assigned_person_id")
	public User getAssignedPerson() {
		return assignedPerson;
	}
	public void setAssignedPerson(User assignedToPerson) {
		this.assignedPerson = assignedToPerson;
	}

	@ManyToOne
	@JoinColumn(name="complaint_id")
	public Complaint getComplaintRef() {
		return complaintRef;
	}
	public void setComplaintRef(Complaint complaint) {
		this.complaintRef = complaint;
	}
	public LocalDateTime getAssignedDateTime() {
		return assignedDateTime;
	}
	public void setAssignedDateTime(LocalDateTime assignedDateTime) {
		this.assignedDateTime = assignedDateTime;
	}
	
	
}
