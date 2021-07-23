package com.ndrmf.setting.dto;

public class DepartmentItem {
	private int id;
	private String name;
	private boolean enabled;
	
	public DepartmentItem(int id, String name, boolean enabled) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
