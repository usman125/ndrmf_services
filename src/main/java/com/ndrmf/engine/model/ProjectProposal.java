package com.ndrmf.engine.model;

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
@Table(name = "project_proposals")
public class ProjectProposal extends Auditable<String> {
	private UUID id;
	private String name;
	private User initiatedBy;
	private User processOwner;
	private String status;
	private List<ProjectProposalSection> sections;
	
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
	
	@OneToMany(mappedBy="proposalRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ProjectProposalSection> getSections() {
		return sections;
	}
	
	public void setSections(List<ProjectProposalSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(ProjectProposalSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<ProjectProposalSection>();
		}
		
		section.setProposalRef(this);
		
		this.sections.add(section);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
