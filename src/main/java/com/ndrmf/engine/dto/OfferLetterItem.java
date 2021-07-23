package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.UUID;

import com.ndrmf.common.rFile;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.user.dto.UserLookupItem;


public class OfferLetterItem {
	private UUID id;
	private UUID proposalRefId;
	private ProjectProposal proposalRef;
	private rFile fileRef;
	private String stage;
	private String data;
	private String status;
	private String gmMarkingStatus;	//true in case of offer letter.
	private String fipMarkingStatus;
	private String GM_comments;
	private String FIP_comments;
	private String gmResponse;
	private String fipResponse;
	private Date expiryDate;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}
	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public UUID getProposalRefId() {
		return proposalRefId;
	}
	public void setProposalRefId(UUID proposalRefId) {
		this.proposalRefId = proposalRefId;
	}

	public void setGmResponse(String gmResponse) { this.gmResponse = gmResponse; }
	public String getGmResponse() { return gmResponse; }

	public void setFipResponse(String fipResponse) {
		this.fipResponse = fipResponse;
	}

	public String getFipResponse() {
		return fipResponse;
	}




}
