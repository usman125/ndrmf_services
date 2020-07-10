package com.ndrmf.engine.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "extended_appraisals")
public class ExtendedAppraisal extends Auditable<String> {
	private UUID id;
	private String status;
	private User assignee;
	private Date startDate;
	private Date endDate;
	private Date completedOn;
	private String comments;
	private String subStatus;
	private String isMarkedTo;
	
	private ProjectProposal proposalRef;
	private List<ExtendedAppraisalSection> sections;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "assignee_user_id", nullable = false)
	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@OneToOne(mappedBy = "preAppraisal")
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}

	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}

	@OneToMany(mappedBy="extendedAppraisalRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ExtendedAppraisalSection> getSections() {
		return sections;
	}

	public void setSections(List<ExtendedAppraisalSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(ExtendedAppraisalSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<ExtendedAppraisalSection>();
		}
		
		section.setExtendedAppraisalRef(this);
		this.sections.add(section);
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCompletedOn() {
		return completedOn;
	}
	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public String getIsMarkedTo() {
		return isMarkedTo;
	}

	public void setIsMarkedTo(String isMarkedTo) {
		this.isMarkedTo = isMarkedTo;
	}
}
