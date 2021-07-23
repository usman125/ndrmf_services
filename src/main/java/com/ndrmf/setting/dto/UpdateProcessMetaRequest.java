package com.ndrmf.setting.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class UpdateProcessMetaRequest {
	private UUID processOwnerId;
	private List<SectionMeta> sections;

	public UUID getProcessOwnerId() {
		return processOwnerId;
	}

	public void setProcessOwnerId(UUID processOwnerId) {
		this.processOwnerId = processOwnerId;
	}

	public List<SectionMeta> getSections() {
		return sections;
	}

	public void setSections(List<SectionMeta> sections) {
		this.sections = sections;
	}

	public static class SectionMeta {
		private UUID id;
		private UUID smeId;
		
		
		@NotNull(message = "Specifiy SME User for each section")
		public UUID getSmeId() {
			return smeId;
		}
		public void setSmeId(UUID smeId) {
			this.smeId = smeId;
		}
		
		@NotNull(message = "Section ID cannot be null or empty")
		public UUID getId() {
			return id;
		}
		public void setId(UUID id) {
			this.id = id;
		}
	}
}
