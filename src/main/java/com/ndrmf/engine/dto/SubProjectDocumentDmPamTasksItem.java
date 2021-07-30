package com.ndrmf.engine.dto;

import com.ndrmf.engine.model.SubProjectDocumentTasks;
import com.ndrmf.user.dto.UserLookupItem;
//import com.ndrmf.user.dto.;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SubProjectDocumentDmPamTasksItem {
	private UUID id;
	private String status;
	private String subStatus;
	private UserLookupItem assignee;
	private UserLookupItem assigneedBy;
	private Date startDate;
	private Date endDate;
	private Date completedOn;
	private UUID subProjectRef;
	private List<SubProjectDocumentTasksItem> tasks;
	private List<GeneralCommentItem> commentsMatrix;
	private String comments;
	private String dmComments;

	public List<GeneralCommentItem> getCommentsMatrix() {
		return commentsMatrix;
	}

	public void setCommentsMatrix(List<GeneralCommentItem> commentsMatrix) {
		this.commentsMatrix = commentsMatrix;
	}

	public void addComment(GeneralCommentItem comment) {
		if(this.commentsMatrix == null) {
			this.commentsMatrix = new ArrayList<>();
		}

		this.commentsMatrix.add(comment);
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

	public UUID getSubProjectRef() {
		return subProjectRef;
	}

	public void setSubProjectRef(UUID subProjectRef) {
		this.subProjectRef = subProjectRef;
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

	public List<SubProjectDocumentTasksItem> getTasks() {
		return tasks;
	}

	public void setTasks(List<SubProjectDocumentTasksItem> tasks) {
		this.tasks = tasks;
	}

	public void addTask(SubProjectDocumentTasksItem spdt){
		if (this.tasks == null)
			this.tasks = new ArrayList<>();

		this.tasks.add(spdt);
	}

	public String getDmComments() {
		return dmComments;
	}

	public void setDmComments(String dmComments) {
		this.dmComments = dmComments;
	}
}
