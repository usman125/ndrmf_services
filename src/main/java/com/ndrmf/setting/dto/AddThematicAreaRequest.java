package com.ndrmf.setting.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

public class AddThematicAreaRequest {
	private String name;
	private UUID processOwnerId;
	private boolean enabled;

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getProcessOwnerId() {
		return processOwnerId;
	}

	public void setProcessOwnerId(UUID processOwnerId) {
		this.processOwnerId = processOwnerId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
