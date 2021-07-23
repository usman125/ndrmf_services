package com.ndrmf.engine.dto;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.dto.UserLookupItem;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

public class ProjectClosureTasksItem extends Auditable<String> {
	private UUID id;
	private UserLookupItem initiatedBy;
	private UserLookupItem assignee;
	private String status;
	private String generalFields;
	private Date created_at;

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public UserLookupItem getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(UserLookupItem initiatedBy) {
		this.initiatedBy = initiatedBy;
	}


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public UserLookupItem getAssignee() {
		return assignee;
	}
	public void setAssignee(UserLookupItem assignee) {
		this.assignee = assignee;
	}

	public String getGeneralFields() {
		return generalFields;
	}
	public void setGeneralFields(String generalFields) {
		this.generalFields = generalFields;
	}

	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
