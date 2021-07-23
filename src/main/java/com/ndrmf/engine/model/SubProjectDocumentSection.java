package com.ndrmf.engine.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ndrmf.setting.model.Section;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "subproject_document_sections")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
public class SubProjectDocumentSection {
	private UUID id;
	private String templateType;
	private String template;
	private String data;
	private SubProjectDocument subProjectDocumentRef;
	private Section sectionRef;
	private User sme;
	private String comments;
	private String status;
	private String reviewStatus;
	private Date reviewCompletedOn;
	private String reassignmentStatus;
	private String reassignmentComments;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
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
	
	@ManyToOne
	@JoinColumn(name = "subproject_document_id", nullable = false)
	public SubProjectDocument getSubProjectDocumentRef() {
		return subProjectDocumentRef;
	}
	public void setSubProjectDocumentRef(SubProjectDocument subProjectDocumentRef) {
		this.subProjectDocumentRef = subProjectDocumentRef;
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
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
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

	public String getReassignmentStatus() {
		return reassignmentStatus;
	}

	public void setReassignmentStatus(String reassignmentStatus) {
		this.reassignmentStatus = reassignmentStatus;
	}

	public String getReassignmentComments() {
		return reassignmentComments;
	}

	public void setReassignmentComments(String reassignmentComments) {
		this.reassignmentComments = reassignmentComments;
	}
}
