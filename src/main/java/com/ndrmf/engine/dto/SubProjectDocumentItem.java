package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class SubProjectDocumentItem {
	private Date startDate;
	private Date endDate;
	private String status;
	private List<SubProjectDocumentSectionItem> sections;
	private List<SubProjectDocumentDmPamTasksItem> dmpamTasks;
	private String docName;
	private String docNumber;

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

	public List<SubProjectDocumentSectionItem> getSections() {
		return sections;
	}

	public void setSections(List<SubProjectDocumentSectionItem> sections) {
		this.sections = sections;
	}
	
	public void addSection(SubProjectDocumentSectionItem section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		this.sections.add(section);
	}

	public List<SubProjectDocumentDmPamTasksItem> getDmpamTasks() {
		return dmpamTasks;
	}

	public void setDmpamTasks(List<SubProjectDocumentDmPamTasksItem> dmpamTasks) {
		this.dmpamTasks = dmpamTasks;
	}
	public void addTask(SubProjectDocumentDmPamTasksItem task) {
		if(this.dmpamTasks == null) {
			this.dmpamTasks = new ArrayList<>();
		}

		this.dmpamTasks.add(task);
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

	public static class SubProjectDocumentSectionItem {
		private UUID id;
		private String templateType;
		private String template;
		private String data;
		private UserLookupItem sme;
		private boolean assigned;
		private String status;
		private String comments;
		private String reviewStatus;
		private String sectionName;
		private String reassignmentStatus;
		private String reassignmentComments;
		private Date reviewCompletedOn;
		
		public UUID getId() {
			return id;
		}
		public void setId(UUID id) {
			this.id = id;
		}
		public String getTemplateType() {
			return templateType;
		}
		public void setTemplateType(String templateType) {
			this.templateType = templateType;
		}
		public String getTemplate() {
			return template;
		}
		public void setTemplate(String template) {
			this.template = template;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public UserLookupItem getSme() {
			return sme;
		}
		public void setSme(UserLookupItem sme) {
			this.sme = sme;
		}
		public boolean isAssigned() {
			return assigned;
		}
		public void setAssigned(boolean assigned) {
			this.assigned = assigned;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public String getReviewStatus() {
			return reviewStatus;
		}
		public void setReviewStatus(String reviewStatus) {
			this.reviewStatus = reviewStatus;
		}
		public Date getReviewCompletedOn() {
			return reviewCompletedOn;
		}
		public void setReviewCompletedOn(Date reviewCompletedOn) {
			this.reviewCompletedOn = reviewCompletedOn;
		}

		public String getSectionName() {
			return sectionName;
		}

		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}

		public void setReassignmentStatus(String reassignmentStatus) {
			this.reassignmentStatus = reassignmentStatus;
		}

		public String getReassignmentStatus() {
			return reassignmentStatus;
		}

		public void setReassignmentComments(String reassignmentComments) {
			this.reassignmentComments = reassignmentComments;
		}

		public String getReassignmentComments() {
			return reassignmentComments;
		}
	}
}
