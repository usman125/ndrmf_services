package com.ndrmf.notification.dto;

import com.ndrmf.user.dto.QprUserLookUpItem;
import com.ndrmf.user.dto.UserLookupItem;

import java.util.Date;
import java.util.UUID;

public class TaskItem {
	private UUID taskId;
	private UUID requestId;
	private UUID sectionId;
	private String sectionName;
	private String status;
	private Date startDate;
	private String fipName;
	
	public UUID getSectionId() {
		return sectionId;
	}
	public void setSectionId(UUID sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getFipName() {
		return fipName;
	}
	public void setFipName(String fipName) {
		this.fipName = fipName;
	}

	public static class TaskItemForQpr{
		private UUID taskId;
		private UUID requestId;
		private UUID sectionId;
		private String sectionName;
		private String status;
		private Date startDate;
		private Date reviewCompletedOn;
		private String othersDecision;
		private String othersRemarks;
		private String fipName;
		private QprUserLookUpItem assignee;

		public UUID getSectionId() {
			return sectionId;
		}
		public void setSectionId(UUID sectionId) {
			this.sectionId = sectionId;
		}
		public String getSectionName() {
			return sectionName;
		}
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
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
		public String getFipName() {
			return fipName;
		}
		public void setFipName(String fipName) {
			this.fipName = fipName;
		}

		public QprUserLookUpItem getAssignee() {
			return assignee;
		}

		public void setAssignee(QprUserLookUpItem assignee) {
			this.assignee = assignee;
		}

		public Date getReviewCompletedOn() {
			return reviewCompletedOn;
		}

		public void setReviewCompletedOn(Date reviewCompletedOn) {
			this.reviewCompletedOn = reviewCompletedOn;
		}

		public String getOthersDecision() {
			return othersDecision;
		}

		public void setOthersDecision(String othersDecision) {
			this.othersDecision = othersDecision;
		}

		public String getOthersRemarks() {
			return othersRemarks;
		}

		public void setOthersRemarks(String othersRemarks) {
			this.othersRemarks = othersRemarks;
		}
	}
}
