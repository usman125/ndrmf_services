package com.ndrmf.complaint.model;

import java.io.Serializable;
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
@Table(name = "complaint_appeal")
public class ComplaintAppeal implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3874649575421500318L;
	private UUID id;
	private Complaint complaintRef;
	private User appealTo;
	private LocalDateTime appealDateTime;
	private String status;
	private String remarks;

	public ComplaintAppeal() {
	}

	public ComplaintAppeal(UUID id, Complaint complaintRef, User appealTo, LocalDateTime appealDateTime, String status,
			String remarks) {
		super();
		this.id = id;
		this.complaintRef = complaintRef;
		this.appealTo = appealTo;
		this.appealDateTime = appealDateTime;
		this.status = status;
		this.remarks = remarks;
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

	@ManyToOne
	@JoinColumn(name="user_id")
	public User getAppealTo() {
		return appealTo;
	}

	public void setAppealTo(User appealTo) {
		this.appealTo = appealTo;
	}

	public LocalDateTime getAppealDateTime() {
		return appealDateTime;
	}

	public void setAppealDateTime(LocalDateTime appealDateTime) {
		this.appealDateTime = appealDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
