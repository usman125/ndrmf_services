package com.ndrmf.util.enums;

public enum ReviewStatus {
	PENDING("Pending"), COMPLETED("Completed");
	
	private final String persistenceValue;
	
	private ReviewStatus(String value) {
		this.persistenceValue = value;
	}
	
	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}
