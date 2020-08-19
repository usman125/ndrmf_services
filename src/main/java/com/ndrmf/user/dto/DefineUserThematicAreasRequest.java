package com.ndrmf.user.dto;

import java.util.Set;
import java.util.UUID;

public class DefineUserThematicAreasRequest {
	private boolean availableAsJv;
	private Set<UUID> areas;
	
	public boolean isAvailableAsJv() {
		return availableAsJv;
	}
	public void setAvailableAsJv(boolean availableAsJv) {
		this.availableAsJv = availableAsJv;
	}
	public Set<UUID> getAreas() {
		return areas;
	}
	public void setAreas(Set<UUID> areas) {
		this.areas = areas;
	}
}
