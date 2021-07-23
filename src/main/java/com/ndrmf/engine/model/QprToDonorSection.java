package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.Section;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "qpr_to_donor_sections")
public class QprToDonorSection extends Auditable<String>{
	private UUID id;
	private String name;
	private Integer totalScore;
	private Integer passingScore;
	private String templateType;
	private String template;
	private String data;
	private int revisionNo;
	private QprToDonor qprtodonorRef;
	private Section sectionRef;
	private User sme;
	private Integer smeScore;
	private String status;
	private String reviewStatus;
	private Date reviewCompletedOn;
	private String reassignmentStatus;
	private String reassignmentComments;
	private List<QprToDonorSectionReview> reviews;
	
	@Id
	@Column(columnDefinition = "uuid", updatable = false)
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
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(int revisionNo) {
		this.revisionNo = revisionNo;
	}
	
	@ManyToOne
	@JoinColumn(name = "qprtodonor_id", nullable = false)
	public QprToDonor getQprtodonorRef() {
		return qprtodonorRef;
	}
	public void setQprtodonorRef(QprToDonor qprtodonorRef) {
		this.qprtodonorRef = qprtodonorRef;
	}
	
	@ManyToOne
	@JoinColumn(name = "ref_section_id")
	public Section getSectionRef() {
		return sectionRef;
	}
	public void setSectionRef(Section sectionRef) {
		this.sectionRef = sectionRef;
	}
	
	@ManyToOne
	@JoinColumn(name = "sme_user_id", nullable = false)
	public User getSme() {
		return sme;
	}
	public void setSme(User sme) {
		this.sme = sme;
	}
	
	public Integer getSmeScore() {
		return smeScore;
	}
	public void setSmeScore(Integer smeScore) {
		this.smeScore = smeScore;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	
	public String getReassignmentStatus() {
		return reassignmentStatus;
	}
	public void setReassignmentStatus(String reassignmentStatus) {
		this.reassignmentStatus = reassignmentStatus;
	}
	
	@OneToMany(mappedBy="sectionRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<QprToDonorSectionReview> getReviews() {
		return reviews;
	}
	public void setReviews(List<QprToDonorSectionReview> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(QprToDonorSectionReview review) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<>();
		}
		
		review.setSectionRef(this);
		this.reviews.add(review);
	}
	
	@PrePersist
	public void prePersist() {
		if(this.id == null) {
			this.id = UUID.randomUUID();
		}
		
		this.revisionNo = 1;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getReviewCompletedOn() {
		return reviewCompletedOn;
	}
	public void setReviewCompletedOn(Date reviewCompletedOn) {
		this.reviewCompletedOn = reviewCompletedOn;
	}

	public String getReassignmentComments() {
		return reassignmentComments;
	}

	public void setReassignmentComments(String reassignmentComments) {
		this.reassignmentComments = reassignmentComments;
	}
}
