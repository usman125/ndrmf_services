package com.ndrmf.engine.dto;

import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class AccreditationQuestionairreListItem {
	private UUID id;
	private UserLookupItem forUser;
	private UserLookupItem assignee;
	private boolean assigned;
	private String status;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UserLookupItem getForUser() {
		return forUser;
	}
	public void setForUser(UserLookupItem forUser) {
		this.forUser = forUser;
	}
	public UserLookupItem getAssignee() {
		return assignee;
	}
	public void setAssignee(UserLookupItem assignee) {
		this.assignee = assignee;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
