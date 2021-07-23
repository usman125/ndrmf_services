package com.ndrmf.engine.model;


import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ndrmf.common.rFile;
import com.ndrmf.config.audit.Auditable;

@Entity
@Table(name = "project_proposal_attachments")
public class ProjectProposalAttachment extends Auditable<String> {
	private long id;
	private ProjectProposal proposalRef;
	private rFile fileRef;
	private String stage;
	private byte[] picByte;
	private String gmMarkingStatus;	//true in case of offer letter.
	private String fipMarkingStatus;
	
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
	public rFile getFileRef() {
		return fileRef;
	}
	public void setFileRef(rFile fileRef) {
		this.fileRef = fileRef;
	}
	
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public String getGmMarkingStatus() {
		return gmMarkingStatus;
	}
	public void setGmMarkingStatus(String gmMarkingStatus) {
		this.gmMarkingStatus = gmMarkingStatus;
	}
	public String getFipMarkingStatus() {
		return fipMarkingStatus;
	}
	public void setFipMarkingStatus(String fipMarkingStatus) {
		this.fipMarkingStatus = fipMarkingStatus;
	}

	public byte[] getPicByte() {
		return picByte;
	}
	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

}
