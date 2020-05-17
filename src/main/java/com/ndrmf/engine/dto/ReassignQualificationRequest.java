package com.ndrmf.engine.dto;

import java.util.Set;
import java.util.UUID;

public class ReassignQualificationRequest extends AddQualificationTaskRequest{
	private Set<UUID> sectionIds;

	public Set<UUID> getSectionIds() {
		return sectionIds;
	}

	public void setSectionIds(Set<UUID> sectionIds) {
		this.sectionIds = sectionIds;
	}
}
