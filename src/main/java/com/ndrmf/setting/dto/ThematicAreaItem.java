package com.ndrmf.setting.dto;

import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class ThematicAreaItem {
	private UUID id;
	private String name;
	private UserLookupItem processOwner;
	private boolean enabled;
	
	public ThematicAreaItem() {
		
	}
	
	public ThematicAreaItem(UUID id, String name, UserLookupItem processOwner, boolean enabled) {
		this.id = id;
		this.name = name;
		this.processOwner = processOwner;
		this.enabled = enabled;
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
	public UserLookupItem getProcessOwner() {
		return processOwner;
	}
	public void setProcessOwner(UserLookupItem processOwner) {
		this.processOwner = processOwner;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
