package com.ndrmf.engine.dto;

import java.util.UUID;

public class CommenceProjectProposalRequest {
	private UUID thematicAreaId;
	private String type;
	
	public UUID getThematicAreaId() {
		return thematicAreaId;
	}
	public void setThematicAreaId(UUID thematicAreaId) {
		this.thematicAreaId = thematicAreaId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
