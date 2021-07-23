package com.ndrmf.util.enums;

public enum ReassignmentStatus {
	PENDING("Pending"), COMPLETED("Completed");
	
	private final String persistenceValue;
	
	private ReassignmentStatus(String value) {
		this.persistenceValue = value;
	}
	
	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}