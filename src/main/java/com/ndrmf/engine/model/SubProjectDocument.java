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
@Table(name = "subproject_documents")
public class SubProjectDocument extends Auditable<String> {
	private UUID id;
	private String status;
	private User processOwner;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate completedOn;
	
	private ProjectProposal proposalRef;
	private List<SubProjectDocumentSection> sections;
	
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
	@JoinColumn(name = "proposal_id", nullable = false)
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}
	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}
	
	@OneToMany(mappedBy="subProjectDocumentRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<SubProjectDocumentSection> getSections() {
		return sections;
	}
	public void setSections(List<SubProjectDocumentSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(SubProjectDocumentSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<SubProjectDocumentSection>();
		}
		
		section.setSubProjectDocumentRef(this);
		
		this.sections.add(section);
	}
	
	@ManyToOne
	@JoinColumn(name = "owner_user_id", nullable = false)
	public User getProcessOwner() {
		return processOwner;
	}
	public void setProcessOwner(User processOwner) {
		this.processOwner = processOwner;
	}
	
	@Column(columnDefinition = "DATE")
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	@Column(columnDefinition = "DATE")
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	@Column(columnDefinition = "DATE")
	public LocalDate getCompletedOn() {
		return completedOn;
	}
	public void setCompletedOn(LocalDate completedOn) {
		this.completedOn = completedOn;
	}
}
