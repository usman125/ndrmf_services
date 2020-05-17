package com.ndrmf.util.enums;

public enum ProcessStatus {
	DRAFT("Draft"),
	UNDER_REVIEW("Under Review"),
	APPROVED("Approved"),
	DEFERRED("Deferred"),
	REJECTED("Rejected"),
	NOT_INITIATED("Not Initiated"),
	REASSIGNED("Re Assigned");
	
	private final String persistenceValue;
	
	private ProcessStatus(String value) {
		this.persistenceValue = value;
	}
	
	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}
