package com.ndrmf.user.dto;

import javax.validation.constraints.NotNull;

public class UpdateUserRequest {
	private boolean enabled;

	@NotNull
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
