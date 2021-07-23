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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "project_proposal_tasks")
public class ProjectProposalTask extends Auditable<String> {
	private UUID id;
	private Date startDate;
	private Date endDate;
	private String comments;
	private User assignee;
	private ProjectProposalSection section;
	private ProjectProposal proposal;
	private String status;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
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
	
	@ManyToOne
	@JoinColumn(name = "assignee_user_id", nullable = false)
	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	
	@ManyToOne
	@JoinColumn(name = "proposal_section_id")
	public ProjectProposalSection getSection() {
		return section;
	}
	public void setSection(ProjectProposalSection section) {
		this.section = section;
	}
	
	@ManyToOne
	@JoinColumn(name = "proposal_id")
	public ProjectProposal getProposal() {
		return proposal;
	}
	public void setProposal(ProjectProposal proposal) {
		this.proposal = proposal;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
