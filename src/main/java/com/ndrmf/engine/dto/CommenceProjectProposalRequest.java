package com.ndrmf.engine.dto;

import java.util.UUID;

public class CommenceProjectProposalRequest {
	private String name;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
