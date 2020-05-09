package com.ndrmf.engine.dto;

import java.util.UUID;

public class QualificationSectionRequest {
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
