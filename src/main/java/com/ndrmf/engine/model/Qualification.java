package com.ndrmf.engine.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import com.ndrmf.util.enums.ProcessStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "qualifications")
public class Qualification extends Auditable<String>{
	private UUID id;
	private User initiatedBy;
	private User processOwner;
	private String status;
	private List<QualificationSection> sections;
	private Date expiryDate;
	private String comment;
	private String subStatus;
	private ProcessStatus markedTo;
	private String reportUsers;
//	private date
	
	public ProcessStatus getMarkedTo() {
		return markedTo;
	}
	public void setMarkedTo(ProcessStatus markedTo) {
		this.markedTo = markedTo;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "initiator_user_id")
	public User getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(User initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	
	@ManyToOne
	@JoinColumn(name = "owner_user_id")
	public User getProcessOwner() {
		return processOwner;
	}
	public void setProcessOwner(User processOwner) {
		this.processOwner = processOwner;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@OneToMany(mappedBy="qualifcationRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<QualificationSection> getSections() {
		return sections;
	}
	
	public void setSections(List<QualificationSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(QualificationSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		section.setQualifcationRef(this);
		this.sections.add(section);
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	//public void addComments(String comment) {
	//	this.comments.add(comment);
	//}
	
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	@Override
	public Date getLastModifiedDate() {
		return super.getLastModifiedDate();
	}

	@Override
	public void setLastModifiedDate(Date lastModifiedDate) {
		super.setLastModifiedDate(lastModifiedDate);
	}

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getReportUsers() {
		return reportUsers;
	}

	public void setReportUsers(String reportUsers) {
		this.reportUsers = reportUsers;
	}
}
