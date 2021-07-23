package com.ndrmf.engine.dto;

import java.util.Set;
import java.util.UUID;

public class ReassignPrposalToFIPRequest {
	private Set<UUID> sectionIds;
	private String comments;

	public Set<UUID> getSectionIds() {
		return sectionIds;
	}

	public void setSectionIds(Set<UUID> sectionIds) {
		this.sectionIds = sectionIds;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
