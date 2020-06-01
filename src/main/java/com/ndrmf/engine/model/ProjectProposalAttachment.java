package com.ndrmf.engine.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ndrmf.common.File;

@Entity
@Table(name = "project_proposal_attachments")
public class ProjectProposalAttachment {
	private long id;
	private ProjectProposal proposalRef;
	private File fileRef;
	private String stage;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "proposal_id")
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}
	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}
	
	@ManyToOne
	@JoinColumn(name = "file_id")
	public File getFileRef() {
		return fileRef;
	}
	public void setFileRef(File fileRef) {
		this.fileRef = fileRef;
	}
	
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
}
