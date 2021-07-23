package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.UUID;

public class ProjectProposalListItem {
	private UUID id;
	private String name;
	private String thematicAreaName;
	private String initiatorFullName;
	private Date submittedAt;
	private String status;
	private UUID jvId;
	private Boolean isGov;

	
	public ProjectProposalListItem(UUID id, String name, String thematicAreaName,
			String initiatorFullName, Date submittedAt, String status, UUID jvId) {
		this.id = id;
		this.initiatorFullName = initiatorFullName;
		this.submittedAt = submittedAt;
		this.status = status;
		this.name = name;
		this.thematicAreaName = thematicAreaName;
		this.jvId = jvId;
	}
	
	public ProjectProposalListItem(UUID id, String name, String thematicAreaName,
			String initiatorFullName, Date submittedAt, String status) {
		this.id = id;
		this.initiatorFullName = initiatorFullName;
		this.submittedAt = submittedAt;
		this.status = status;
		this.name = name;
		this.thematicAreaName = thematicAreaName;

	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getInitiatorFullName() {
		return initiatorFullName;
	}
	public void setInitiatorFullName(String initiatorFullName) {
		this.initiatorFullName = initiatorFullName;
	}
	
	public Date getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThematicAreaName() {
		return thematicAreaName;
	}

	public void setThematicAreaName(String thematicAreaName) {
		this.thematicAreaName = thematicAreaName;
	}

	public UUID getJvId() {
		return jvId;
	}

	public void setJvId(UUID jvId) {
		this.jvId = jvId;
	}

	public void setGov(Boolean gov) {
		isGov = gov;
	}

	public Boolean getGov() {
		return isGov;
	}
}
