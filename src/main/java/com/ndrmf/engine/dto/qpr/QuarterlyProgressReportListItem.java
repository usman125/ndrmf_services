package com.ndrmf.engine.dto.qpr;

import java.time.LocalDate;
import java.util.UUID;

public class QuarterlyProgressReportListItem {
	private UUID id;
	private String proposalName;
	private String fipName;
	private int quarter;
	private LocalDate dueDate;
	private String status;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getFipName() {
		return fipName;
	}
	public void setFipName(String fipName) {
		this.fipName = fipName;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
