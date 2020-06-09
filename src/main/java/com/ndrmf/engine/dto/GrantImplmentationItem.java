package com.ndrmf.engine.dto;

import com.ndrmf.user.dto.UserLookupItem;

public class GrantImplmentationItem {
	private UserLookupItem processOwner;
	private String data;
	
	public UserLookupItem getProcessOwner() {
		return processOwner;
	}
	public void setProcessOwner(UserLookupItem processOwner) {
		this.processOwner = processOwner;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
