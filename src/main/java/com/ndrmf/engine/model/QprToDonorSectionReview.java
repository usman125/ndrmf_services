package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qpr_to_donor_reviews")
public class QprToDonorSectionReview extends Auditable<String> {
	private long id;
	private String status;
	private String comments;
	private QprToDonorSection sectionRef;
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
	public QprToDonorSection getSectionRef() {
		return sectionRef;
	}
	public void setSectionRef(QprToDonorSection sectionRef) {
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
