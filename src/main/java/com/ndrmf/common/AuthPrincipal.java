package com.ndrmf.common;

import java.util.List;
import java.util.UUID;

public class AuthPrincipal {
	private final UUID userId;
	private final String email;
	private final String username;
	private final String fullName;
	private final List<String> roles;
	
	public AuthPrincipal(String userId, String email, String username, String fullName, List<String> roles) {
		this.userId = UUID.fromString(userId);
		this.email = email;
		this.username = username;
		this.fullName = fullName;
		this.roles = roles;
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

	public List<String> getRoles() {
		return roles;
	}

	public String getEmail() {
		return email;
	}
}
