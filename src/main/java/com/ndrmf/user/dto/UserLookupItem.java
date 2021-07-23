package com.ndrmf.user.dto;

import java.util.UUID;

public class UserLookupItem {
	private final UUID id;
	private final String name;
	
	public UserLookupItem(UUID id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public UUID getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
