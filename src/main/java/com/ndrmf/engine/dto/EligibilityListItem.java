package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.UUID;

public class EligibilityListItem {
	private UUID id;
	private String initiatorFullName;
	private Date submittedAt;
	private String status;
	private String comment;
	
	public EligibilityListItem(UUID id, String initiatorFullName, Date submittedAt, String status, String comment) {
		this.id = id;
		this.initiatorFullName = initiatorFullName;
		this.submittedAt = submittedAt;
		this.status = status;
		this.comment = comment;
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getInitiatorFullName() {
		return initiatorFullName;
	}
	public void setInitiatorFullName(String initiatorFullName) {
		this.initiatorFullName = initiatorFullName;
	}
	
	public Date getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
