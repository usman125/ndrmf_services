package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddComplaintAssignee {

	private UUID assignedPersonId;
	private LocalDateTime assignedDateTime;
	
	
	public AddComplaintAssignee(UUID assignedPersonId, UUID complaintId, LocalDateTime assignedDateTime) {
		super();
		this.assignedPersonId = assignedPersonId;
		this.assignedDateTime = assignedDateTime;
	}
	public UUID getAssignedPersonId() {
		return assignedPersonId;
	}
	public void setAssignedPersonId(UUID assignedPersonId) {
		this.assignedPersonId = assignedPersonId;
	}
	public LocalDateTime getAssignedDateTime() {
		return assignedDateTime;
	}
	public void setAssignedDateTime(LocalDateTime assignedDateTime) {
		this.assignedDateTime = assignedDateTime;
	}
	
	
	
}
