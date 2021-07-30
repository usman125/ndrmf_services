package com.ndrmf.engine.dto;

import com.ndrmf.user.dto.UserLookupItem;

import java.util.Date;
import java.util.UUID;

public class SubProjectDocumentTasksListItem {
	private UUID id;
	private String status;
	private String subStatus;
	private UserLookupItem assignee;
	private UserLookupItem assigneedBy;
	private Date startDate;
	private Date endDate;
	private Date completedOn;
	private UUID subProjectDmpamTaskRef;
	private UUID subProjectRef;
	private String comments;
	private String docName;
	private String docNumber;
	private String fipName;

	public String getFipName() {
		return fipName;
	}

	public void setFipName(String fipName) {
		this.fipName = fipName;
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public Date getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public UUID getSubProjectDmpamTaskRef() {
		return subProjectDmpamTaskRef;
	}

	public void setSubProjectDmpamTaskRef(UUID subProjectDmpamTaskRef) {
		this.subProjectDmpamTaskRef = subProjectDmpamTaskRef;
	}

	public UserLookupItem getAssignee() {
		return assignee;
	}

	public void setAssignee(UserLookupItem assignee) {
		this.assignee = assignee;
	}

	public UserLookupItem getAssigneedBy() {
		return assigneedBy;
	}

	public void setAssigneedBy(UserLookupItem assigneedBy) {
		this.assigneedBy = assigneedBy;
	}

	public UUID getSubProjectRef() {
		return subProjectRef;
	}

	public void setSubProjectRef(UUID subProjectRef) {
		this.subProjectRef = subProjectRef;
	}


	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

}
