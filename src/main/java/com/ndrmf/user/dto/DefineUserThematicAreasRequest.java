package com.ndrmf.user.dto;

import com.ndrmf.engine.dto.FipThematicAreasInfoList;

import java.util.Set;
import java.util.UUID;

public class DefineUserThematicAreasRequest {
	private boolean availableAsJv;
	private Set<FipThematicAreasInfoList> areas;
	private String jvUser;
	
	public boolean isAvailableAsJv() {
		return availableAsJv;
	}
	public void setAvailableAsJv(boolean availableAsJv) {
		this.availableAsJv = availableAsJv;
	}
//	public Set<UUID> getAreas() {
//		return areas;
//	}
//	public void setAreas(Set<UUID> areas) {
//		this.areas = areas;
//	}


	public Set<FipThematicAreasInfoList> getAreas() {
		return areas;
	}

	public void setAreas(Set<FipThematicAreasInfoList> areas) {
		this.areas = areas;
	}

	public String getJvUser() {
		return jvUser;
	}

	public void setJvUser(String jvUser) {
		this.jvUser = jvUser;
	}
}
