package com.ndrmf.engine.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class QualificationItem {
	private UserLookupItem initiatedBy;
	private UserLookupItem processOwner;
	private String status;
	private Date submittedAt;
	private List<Section> sections;
	private boolean owner;
	
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

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
	
	public void addSection(Section section) {
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

	public static class Section{
		private UUID id;
		private String name;
		private Integer totalScore;
		private Integer passingScore;
		private String templateType;
		private String template;
		private String data;
		private UserLookupItem sme;
		private boolean assigned;
		private Review review;
		
		
		public UUID getId() {
			return id;
		}
		public void setId(UUID id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getTotalScore() {
			return totalScore;
		}
		public void setTotalScore(Integer totalScore) {
			this.totalScore = totalScore;
		}
		public Integer getPassingScore() {
			return passingScore;
		}
		public void setPassingScore(Integer passingScore) {
			this.passingScore = passingScore;
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
		public Review getReview() {
			return review;
		}
		
		public void setReview(String controlWiseComments, String rating, String status, String comments) {
			this.review = new Review();
			
			this.review.setControlWiseComments(controlWiseComments);
			this.review.setRating(rating);
			this.review.setStatus(status);
			this.review.setComments(comments);
		}
	}
	
	public static class Review{
		private String controlWiseComments;
		private String rating;
		private String status;
		private String comments;
		
		public String getControlWiseComments() {
			return controlWiseComments;
		}
		public void setControlWiseComments(String controlWiseComments) {
			this.controlWiseComments = controlWiseComments;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
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
