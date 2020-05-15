package com.ndrmf.notification.dto;

import java.util.Date;
import java.util.UUID;

public class TaskItem {
	private UUID taskId;
	private UUID requestId;
	private Date startDate;
	private Date endDate;
	private String comments;
	
	public UUID getTaskId() {
		return taskId;
	}
	public void setTaskId(UUID taskId) {
		this.taskId = taskId;
	}
	public UUID getRequestId() {
		return requestId;
	}
	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
