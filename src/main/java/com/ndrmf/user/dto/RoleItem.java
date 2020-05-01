package com.ndrmf.user.dto;

public class RoleItem {
	private final int id;
	private final String name;
	
	public RoleItem(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
