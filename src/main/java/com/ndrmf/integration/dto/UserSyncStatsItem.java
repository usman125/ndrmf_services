package com.ndrmf.integration.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserSyncStatsItem {
	private int inserted, updated;
	private Set<SAPUserItem> rejectedUsers;
	
	public int getInserted() {
		return inserted;
	}
	
	@JsonIgnore
	public int incrementInserted() {
		this.inserted += 1;
		
		return this.inserted;
	}
	
	public int getUpdated() {
		return updated;
	}
	
	public int incrementUpdated() {
		this.updated += 1;
		
		return this.updated;
	}
	
	public Set<SAPUserItem> getRejectedUsers() {
		return rejectedUsers;
	}
		
	public void addRejectedUser(SAPUserItem item) {
		if(this.rejectedUsers == null) {
			this.rejectedUsers = new HashSet<>();
		}
		
		this.rejectedUsers.add(item);
	}
}