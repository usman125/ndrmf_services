package com.ndrmf.engine.dto;

import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.user.dto.UserLookupItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QprToDonorItem {
	private UserLookupItem initiatedBy;
	private String status;
	private Date submittedAt;
	private List<SectionItem> sections;
	private TaskItem reassignmentTask;
	private List<GeneralCommentItem> commentsMatrix;

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

	public Date getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}

	public List<SectionItem> getSections() {
		return sections;
	}

	public void setSections(List<SectionItem> sections) {
		this.sections = sections;
	}
	
	public void addSection(SectionItem section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		this.sections.add(section);
	}

	public TaskItem getReassignmentTask() {
		return reassignmentTask;
	}

	public void setReassignmentTask(TaskItem reassignmentTask) {
		this.reassignmentTask = reassignmentTask;
	}

}
