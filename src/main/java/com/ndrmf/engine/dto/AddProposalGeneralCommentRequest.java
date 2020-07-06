package com.ndrmf.engine.dto;

import java.util.Set;
import java.util.UUID;

public class AddProposalGeneralCommentRequest {
	private Set<UUID> sectionIds;
	private String stage;
	private String comment;
	public Set<UUID> getSectionIds() {
		return sectionIds;
	}
	public void setSectionIds(Set<UUID> sectionIds) {
		this.sectionIds = sectionIds;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
}
