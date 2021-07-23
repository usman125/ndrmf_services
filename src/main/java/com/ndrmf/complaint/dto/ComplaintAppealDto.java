package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ComplaintAppealDto {

	private UUID complaintId;
	private UUID appealTo;
	private LocalDateTime appealDateTime;
	private String status;
	private String remarks;
	private UUID id;
	
	public ComplaintAppealDto() {}
	public ComplaintAppealDto(UUID complaintId, UUID appealTo, LocalDateTime appealDateTime, String status,
			String remarks,UUID id) {
		super();
		this.complaintId = complaintId;
		this.appealTo = appealTo;
		this.appealDateTime = appealDateTime;
		this.status = status;
		this.remarks = remarks;
		this.id=id;
	}
	public UUID getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(UUID complaintId) {
		this.complaintId = complaintId;
	}
	public UUID getAppealTo() {
		return appealTo;
	}
	public void setAppealTo(UUID appealTo) {
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
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	
}
