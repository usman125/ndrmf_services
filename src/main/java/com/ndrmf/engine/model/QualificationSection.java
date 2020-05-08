package com.ndrmf.engine.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "qualification_sections")
public class QualificationSection extends Auditable<String>{
	private UUID id;
	private String name;
	private Integer totalScore;
	private Integer passingScore;
	private String templateType;
	private String template;
	private String data;
	private int revisionNo;
	private Qualification qualifcationRef;
	private User sme;
	private Integer smeScore;
	
	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
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
	@JoinColumn(name = "sme_user_id", nullable = false)
	public User getSme() {
		return sme;
	}
	public void setSme(User sme) {
		this.sme = sme;
	}
	
	@ManyToOne
	@JoinColumn(name = "qualification_id", nullable = false)
	public Qualification getQualifcationRef() {
		return qualifcationRef;
	}
	public void setQualifcationRef(Qualification qualifcationRef) {
		this.qualifcationRef = qualifcationRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@PrePersist
	public void prePersist() {
		if(this.id == null) {
			this.id = UUID.randomUUID();
		}
		
		this.revisionNo = 1;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.revisionNo += 1;
	}

	public Integer getSmeScore() {
		return smeScore;
	}

	public void setSmeScore(Integer smeScore) {
		this.smeScore = smeScore;
	}
}
