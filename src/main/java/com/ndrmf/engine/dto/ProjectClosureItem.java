package com.ndrmf.engine.dto;

import com.ndrmf.user.dto.UserLookupItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectClosureItem {
	private UserLookupItem initiatedBy;
	private String status;
	private Date submittedAt;


	public UserLookupItem getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(UserLookupItem initiatedBy) {
		this.initiatedBy = initiatedBy;
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
