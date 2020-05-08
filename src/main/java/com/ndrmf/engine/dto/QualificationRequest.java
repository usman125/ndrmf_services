package com.ndrmf.engine.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class QualificationRequest {
	private List<QualSection> sections;

	@NotNull
	public List<QualSection> getSections() {
		return sections;
	}

	public void setSections(List<QualSection> sections) {
		this.sections = sections;
	}

	public static class QualSection{
		private UUID id;
		private String data;

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}
}
