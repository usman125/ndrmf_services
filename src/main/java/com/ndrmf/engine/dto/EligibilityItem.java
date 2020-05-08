package com.ndrmf.engine.dto;

import java.util.Date;

import com.ndrmf.user.dto.UserLookupItem;

public class EligibilityItem {
	private UserLookupItem initiatedBy;
	private UserLookupItem processOwner;
	private Date submittedAt;
	private String template;
	private String data;
	private String status;
	
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
}
