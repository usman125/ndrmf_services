package com.ndrmf.user.dto;

import java.util.UUID;

public class SmeLookupItem {
	private final UUID id;
	private final String name;
	private final String designation;

	public SmeLookupItem(UUID id, String name, String designation) {
		this.id = id;
		this.name = name;
		this.designation = designation;
	}

	public UUID getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public String getDesignation() {
		return designation;
	}
}
