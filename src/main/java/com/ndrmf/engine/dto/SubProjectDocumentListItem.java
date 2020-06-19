package com.ndrmf.engine.dto;

import java.time.LocalDate;
import java.util.UUID;

public class SubProjectDocumentListItem {
	private UUID id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	private String proposalName;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
}
