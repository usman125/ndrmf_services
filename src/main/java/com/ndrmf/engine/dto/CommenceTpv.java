package com.ndrmf.engine.dto;

import java.util.UUID;

public class CommenceTpv {

	private UUID proposalId;
	private String comments;

	public UUID getProposalId() {
		return proposalId;
	}
	public void setProposalId(UUID proposalId) {
		this.proposalId = proposalId;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comment) {
		this.comments = comment;
	}
	
}
