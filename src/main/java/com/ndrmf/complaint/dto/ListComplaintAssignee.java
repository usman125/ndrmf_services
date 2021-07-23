package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ListComplaintAssignee {

	private UUID assignedPerson;
	private LocalDateTime assignedTimestamp;
	private String assignedPersonName;
	private String email;
	private String org;
	
	
	public ListComplaintAssignee(UUID assignedPerson, LocalDateTime assignedTimestamp, String assignedPersonName,
			String email, String org) {
		super();
		this.assignedPerson = assignedPerson;
		this.assignedTimestamp = assignedTimestamp;
		this.assignedPersonName = assignedPersonName;
		this.email = email;
		this.org = org;
	}
	public UUID getAssignedPerson() {
		return assignedPerson;
	}
	public void setAssignedPerson(UUID assignedPerson) {
		this.assignedPerson = assignedPerson;
	}
	public LocalDateTime getAssignedTimestamp() {
		return assignedTimestamp;
	}
	public void setAssignedTimestamp(LocalDateTime assignedTimestamp) {
		this.assignedTimestamp = assignedTimestamp;
	}
	public String getAssignedPersonName() {
		return assignedPersonName;
	}
	public void setAssignedPersonName(String assignedPersonName) {
		this.assignedPersonName = assignedPersonName;
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
