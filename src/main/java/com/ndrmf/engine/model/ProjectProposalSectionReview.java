package com.ndrmf.engine.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "project_proposal_reviews")
public class ProjectProposalSectionReview extends Auditable<String> {
	private long id;
	private String status;
	private String comments;
	private ProjectProposalSection sectionRef;
	private Date createdDate;
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
	@JoinColumn(name = "proposal_section_id")
	public ProjectProposalSection getSectionRef() {
		return sectionRef;
	}
	public void setSectionRef(ProjectProposalSection sectionRef) {
		this.sectionRef = sectionRef;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@PrePersist
	public void prePersist() {
		this.createdDate = new Date();
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
