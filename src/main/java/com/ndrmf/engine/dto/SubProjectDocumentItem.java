package com.ndrmf.engine.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class SubProjectDocumentItem {
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	private List<SubProjectDocumentSectionItem> sections;

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
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

	public static class SubProjectDocumentSectionItem {
		private UUID id;
		private String templateType;
		private String template;
		private String data;
		private UserLookupItem sme;
		private boolean assigned;
		private String status;
		private String comments;
		
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
	}
}
