package com.ndrmf.engine.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "quarterly_progress_report_reviews")
public class QuarterlyProgressReportSectionReview extends Auditable<String> {
	private long id;
	private String status;
	private String comments;
	private QuarterlyProgressReportSection sectionRef;
	private User reviewAddedBy;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	@ManyToOne
	@JoinColumn(name = "qpr_section_id")
	public QuarterlyProgressReportSection getSectionRef() {
		return sectionRef;
	}
	public void setSectionRef(QuarterlyProgressReportSection sectionRef) {
		this.sectionRef = sectionRef;
	}
	
	@ManyToOne
	@JoinColumn(name = "reviewer_user_id")
	public User getReviewAddedBy() {
		return reviewAddedBy;
	}
	public void setReviewAddedBy(User reviewAddedBy) {
		this.reviewAddedBy = reviewAddedBy;
	}
}
