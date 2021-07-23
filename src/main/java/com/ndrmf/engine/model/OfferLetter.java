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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ndrmf.common.rFile;
import com.ndrmf.util.enums.ProcessStatus;

@Entity
@Table(name = "offer_letters")
public class OfferLetter {
	private UUID id;
	private ProjectProposal proposalRef;
	private rFile fileRef;
	private String stage;
	private String data;
	private String status;
	private String gmMarkingStatus;	//true in case of offer letter.
	private String fipMarkingStatus;
	private String GM_comments;
	private String FIP_comments;
	private String GM_response;
	private String FIP_response;
	private Date expiryDate;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isMarkedToGM() {
		return markedToGM;
	}
	public void setMarkedToGM(boolean markedToGM) {
		this.markedToGM = markedToGM;
	}
	private boolean markedToGM;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@OneToOne
	@JoinColumn(name = "offerLetter")
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
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
	public String getGM_comments() {
		return GM_comments;
	}
	public void setGM_comments(String gM_comments) {
		GM_comments = gM_comments;
	}
	public String getFIP_comments() {
		return FIP_comments;
	}
	public void setFIP_comments(String fIP_comments) {
		FIP_comments = fIP_comments;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setGM_response(String GM_response) {
		this.GM_response = GM_response;
	}

	public String getGM_response() {
		return GM_response;
	}

	public void setFIP_response(String FIP_response) {
		this.FIP_response = FIP_response;
	}

	public String getFIP_response() {
		return FIP_response;
	}
}
