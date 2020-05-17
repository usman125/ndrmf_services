package com.ndrmf.engine.model;

import java.util.Date;

import javax.persistence.Column;
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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "qualification_reviews")
public class QualificationSectionReview {
	private long id;
	private String controlWiseComments;
	private String rating;
	private String status;
	private String comments;
	
	private QualificationSection sectionRef;
	
	private Date createdDate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
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

	@ManyToOne
	@JoinColumn(name = "qual_section_id")
	public QualificationSection getSectionRef() {
		return sectionRef;
	}

	public void setSectionRef(QualificationSection sectionRef) {
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
}
