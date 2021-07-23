package com.ndrmf.engine.model;

import java.time.LocalDate;
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


@Entity
@Table(name = "quarterly_progress_reports")
public class QuarterlyProgressReport extends Auditable<String> {
	private UUID id;
	private int quarter;
	private LocalDate dueDate;
	private LocalDate submittedAt;
	private User processOwner;
	private String status;
	private ProjectProposal proposalRef;
	private List<QuarterlyProgressReportSection> sections;
	private String assignedComments;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	
	@Column(columnDefinition = "DATE")
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	@ManyToOne
	@JoinColumn(name = "proposal_id", nullable = false)
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}
	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}
	
	@OneToMany(mappedBy="qprRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<QuarterlyProgressReportSection> getSections() {
		return sections;
	}
	public void setSections(List<QuarterlyProgressReportSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(QuarterlyProgressReportSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		section.setQprRef(this);
		
		this.sections.add(section);
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
	public LocalDate getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(LocalDate submittedAt) {
		this.submittedAt = submittedAt;
	}

	public String getAssignedComments() {
		return assignedComments;
	}

	public void setAssignedComments(String assignedComments) {
		this.assignedComments = assignedComments;
	}
}
