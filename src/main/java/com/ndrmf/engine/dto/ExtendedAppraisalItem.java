package com.ndrmf.engine.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class ExtendedAppraisalItem {
	private UUID id;
	private UserLookupItem assignee;
	private Date startDate;
	private Date endDate;
	private Date completedDate;
	private String comments;
	private boolean assigned;
	private String status;
	private List<ExtendedAppraisalSectionItem> sections;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UserLookupItem getAssignee() {
		return assignee;
	}

	public void setAssignee(UserLookupItem assignee) {
		this.assignee = assignee;
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

	public List<ExtendedAppraisalSectionItem> getSections() {
		return sections;
	}

	public void setSections(List<ExtendedAppraisalSectionItem> sections) {
		this.sections = sections;
	}
	
	public void addSection(ExtendedAppraisalSectionItem section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		this.sections.add(section);
	}
	
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public static class ExtendedAppraisalSectionItem {
		private UUID id;
		private String templateType;
		private String template;
		private String data;
		private UserLookupItem sme;
		private boolean assigned;
		private String status;
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
	}
}
