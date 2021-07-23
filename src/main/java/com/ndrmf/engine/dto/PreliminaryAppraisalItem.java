package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.UUID;

public class PreliminaryAppraisalItem {
	private UUID id;
	private String proposalName;
	private String template;
	private String data;
	private Date startDate;
	private Date endDate;
	private Date completedDate;
	private boolean assigned;
	private String status;
	private String subStatus;
	private String isMarkedTo;
	private String commentsByPo;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
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

	public String getCommentsByPo() {
		return commentsByPo;
	}

	public void setCommentsByPo(String commentsByPo) {
		this.commentsByPo = commentsByPo;
	}
}
