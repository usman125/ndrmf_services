package com.ndrmf.engine.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ndrmf.engine.model.GrantDisbursment;
import com.ndrmf.engine.model.OfferLetter;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.user.dto.UserLookupItem;

public class ProjectProposalItem {
	private UserLookupItem initiatedBy;
	private UserLookupItem processOwner;
	private UserLookupItem jv;
	private String status;
	private String subStatus;
	private Date submittedAt;
	private List<SectionItem> sections;
	private boolean owner;
	private TaskItem reassignmentTask;
	private PreliminaryAppraisalItem preAppraisal;
	private ExtendedAppraisalItem extendedAppraisal;
	private List<GeneralCommentItem> commentsMatrix;
	private String implementationPlan;
	private GrantImplmentationItem gia;
	private GIAChecklistItem giaChecklist;
	private boolean isGovFip;
	private Integer numOfQuarters;
	private OfferLetterItem offerLetter;
	private GrantDisbursment grantDisbursment;
	private String proposalName;
	private String tpvStatus;
	private String pcStatus;
	
	public List<GeneralCommentItem> getCommentsMatrix() {
		return commentsMatrix;
	}

	public void setCommentsMatrix(List<GeneralCommentItem> commentsMatrix) {
		this.commentsMatrix = commentsMatrix;
	}
	
	public void addComment(GeneralCommentItem comment) {
		if(this.commentsMatrix == null) {
			this.commentsMatrix = new ArrayList<>();
		}
		
		this.commentsMatrix.add(comment);
	}

	public UserLookupItem getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(UserLookupItem initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public UserLookupItem getProcessOwner() {
		return processOwner;
	}

	public void setProcessOwner(UserLookupItem processOwner) {
		this.processOwner = processOwner;
	}

	public UserLookupItem getJv() {
		return jv;
	}

	public void setJv(UserLookupItem jv) {
		this.jv = jv;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}

	public List<SectionItem> getSections() {
		return sections;
	}

	public void setSections(List<SectionItem> sections) {
		this.sections = sections;
	}
	
	public void addSection(SectionItem section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		this.sections.add(section);
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public TaskItem getReassignmentTask() {
		return reassignmentTask;
	}

	public void setReassignmentTask(TaskItem reassignmentTask) {
		this.reassignmentTask = reassignmentTask;
	}
	
	public PreliminaryAppraisalItem getPreAppraisal() {
		return preAppraisal;
	}

	public void setPreAppraisal(PreliminaryAppraisalItem preAppraisal) {
		this.preAppraisal = preAppraisal;
	}

	public ExtendedAppraisalItem getExtendedAppraisal() {
		return extendedAppraisal;
	}

	public void setExtendedAppraisal(ExtendedAppraisalItem extendedAppraisal) {
		this.extendedAppraisal = extendedAppraisal;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	
	public String getImplementationPlan() {
		return implementationPlan;
	}

	public void setImplementationPlan(String rawJson) {
		this.implementationPlan = rawJson.toString();
	}

	public GrantImplmentationItem getGia() {
		return gia;
	}

	public void setGia(GrantImplmentationItem gia) {
		this.gia = gia;
	}

	public GIAChecklistItem getGiaChecklist() {
		return giaChecklist;
	}

	public void setGiaChecklist(GIAChecklistItem giaChecklist) {
		this.giaChecklist = giaChecklist;
	}

	public boolean isGovFip() {
		return isGovFip;
	}

	public void setGovFip(boolean isGovFip) {
		this.isGovFip = isGovFip;
	}

	public Integer getNumOfQuarters() {
		return numOfQuarters;
	}

	public void setNumOfQuarters(Integer numOfQuarters) {
		this.numOfQuarters = numOfQuarters;
	}

	public OfferLetterItem getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetterItem offerLetter) {
		this.offerLetter = offerLetter;
	}

	public GrantDisbursment getGrantDisbursment() {
		return grantDisbursment;
	}

	public void setGrantDisbursment(GrantDisbursment grantDisbursment) {
		this.grantDisbursment = grantDisbursment;
	}

	public String getProposalName() {
		return proposalName;
	}

	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}

	public String getTpvStatus() {
		return tpvStatus;
	}

	public void setTpvStatus(String tpvStatus) {
		this.tpvStatus = tpvStatus;
	}

	public String getPcStatus() {
		return pcStatus;
	}

	public void setPcStatus(String pcStatus) {
		this.pcStatus = pcStatus;
	}
}
