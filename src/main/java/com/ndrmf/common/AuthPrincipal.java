package com.ndrmf.common;

import java.util.UUID;

public class AuthPrincipal {
	private final UUID userId;
	private final String username;
	private final String fullName;
	
	public AuthPrincipal(String userId, String username, String fullName) {
		this.userId = UUID.fromString(userId);
		this.username = username;
		this.fullName = fullName;
	}
	
	public UUID getUserId() {
		return userId;
	}
	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}
}
