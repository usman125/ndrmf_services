package com.ndrmf.util;

import java.util.UUID;

public class KeyValue {
	private UUID id;
	private String name;
	
	public KeyValue() {
		
	}
	
	public KeyValue(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
