package com.ndrmf.engine.dto;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.dto.UserLookupItem;
import java.util.Date;
import java.util.UUID;

public class ProjectClosureTasksListItem extends Auditable<String> {
	private UUID id;
	private UserLookupItem initiatedBy;
	private UserLookupItem assignee;
	private String status;
	private String generalFields;
	private Date created_at;
	private boolean assigned;
	private Integer orderNum;
	private String isMarkedTo;
	private String markedToStatus;
	private String markedToComments;
	private String markedToSubStatus;
	private UUID closureId;

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

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getIsMarkedTo() {
		return isMarkedTo;
	}

	public void setIsMarkedTo(String isMarkedTo) {
		this.isMarkedTo = isMarkedTo;
	}

	public String getMarkedToStatus() {
		return markedToStatus;
	}

	public void setMarkedToStatus(String markedToStatus) {
		this.markedToStatus = markedToStatus;
	}

	public String getMarkedToComments() {
		return markedToComments;
	}
	public void setMarkedToComments(String markedToComments) {
		this.markedToComments = markedToComments;
	}

	public String getMarkedToSubStatus() {
		return markedToSubStatus;
	}

	public void setMarkedToSubStatus(String markedToSubStatus) {
		this.markedToSubStatus = markedToSubStatus;
	}

	public UUID getClosureId() {
		return closureId;
	}

	public void setClosureId(UUID closureId) {
		this.closureId = closureId;
	}
}
