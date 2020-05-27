package com.ndrmf.engine.dto;

import java.util.UUID;

public class PreliminaryAppraisalListItem {
	private UUID id;
	private UUID proposalId;
	private String proposalName;
	private String fipName;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getProposalId() {
		return proposalId;
	}
	public void setProposalId(UUID proposalId) {
		this.proposalId = proposalId;
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
}
