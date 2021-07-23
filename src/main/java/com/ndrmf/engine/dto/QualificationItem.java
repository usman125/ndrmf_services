package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.enums.ProcessStatus;

public class QualificationItem {
	private UserLookupItem initiatedBy;
	private UserLookupItem processOwner;
	private String jvUser;
	private String status;
	private Date submittedAt;
	private List<SectionItem> sections;
	private boolean owner;
	private boolean availableAsJv;
	private TaskItem reassignmentTask;
	private Date expiryDate;
	private String comment;
	private String subStatus;
	private ProcessStatus markedTo;
	private Date lastModified;
	private String reviewUsers;
	
	public ProcessStatus getMarkedTo() {
		return markedTo;
	}

	public void setMarkedTo(ProcessStatus markedTo) {
		this.markedTo = markedTo;
	}

	public UserLookupItem getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(UserLookupItem initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public UserLookupItem getProcessOwner() {
		return processOwner;
	}

	public void setProcessOwner(UserLookupItem processOwner) {
		this.processOwner = processOwner;
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

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public boolean isAvailableAsJv() {
		return availableAsJv;
	}

	public void setAvailableAsJv(boolean availableAsJv) {
		this.availableAsJv = availableAsJv;
	}

	public String getJvUser() {
		return jvUser;
	}

	public void setJvUser(String jvUser) {
		this.jvUser = jvUser;
	}

	public TaskItem getReassignmentTask() {
		return reassignmentTask;
	}

	public void setReassignmentTask(TaskItem reassignmentTask) {
		this.reassignmentTask = reassignmentTask;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getReviewUsers() {
		return reviewUsers;
	}

	public void setReviewUsers(String reviewUsers) {
		this.reviewUsers = reviewUsers;
	}
}
