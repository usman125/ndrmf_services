package com.ndrmf.engine.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.ndrmf.setting.model.Section;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "quarterly_progress_report_sections")
public class QuarterlyProgressReportSection {
	private UUID id;
	private String name;
	private String templateType;
	private String template;
	private String data;
	private int revisionNo;
	private QuarterlyProgressReport qprRef;
	private Section sectionRef;
	private User sme;
	private String status;
	private String reviewStatus;
	private Date reviewCompletedOn;
	private String reassignmentStatus;
	
	List<QuarterlyProgressReportSectionReview> reviews;

	
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
	@JoinColumn(name = "qpr_id", nullable = false)
	public QuarterlyProgressReport getQprRef() {
		return qprRef;
	}

	public void setQprRef(QuarterlyProgressReport qprRef) {
		this.qprRef = qprRef;
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

	public Date getReviewCompletedOn() {
		return reviewCompletedOn;
	}

	public void setReviewCompletedOn(Date reviewCompletedOn) {
		this.reviewCompletedOn = reviewCompletedOn;
	}

	public String getReassignmentStatus() {
		return reassignmentStatus;
	}

	public void setReassignmentStatus(String reassignmentStatus) {
		this.reassignmentStatus = reassignmentStatus;
	}

	@OneToMany(mappedBy="sectionRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<QuarterlyProgressReportSectionReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<QuarterlyProgressReportSectionReview> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(QuarterlyProgressReportSectionReview review) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<>();
		}
		
		review.setSectionRef(this);
		
		this.reviews.add(review);
	}
}
