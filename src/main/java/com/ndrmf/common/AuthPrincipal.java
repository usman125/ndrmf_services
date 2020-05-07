package com.ndrmf.common;

import java.util.UUID;

public class AuthPrincipal {
	private final UUID userId;
	private final String username;
	
	public AuthPrincipal(String userId, String username) {
		this.userId = UUID.fromString(userId);
		this.username = username;
	}
	
	public UUID getUserId() {
		return userId;
	}
	public String getUsername() {
		return username;
	}
}
