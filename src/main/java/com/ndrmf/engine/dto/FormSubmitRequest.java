package com.ndrmf.engine.dto;

import java.util.List;
import java.util.UUID;

public class FormSubmitRequest {
	private List<Section> sections;

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public static class Section{
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
