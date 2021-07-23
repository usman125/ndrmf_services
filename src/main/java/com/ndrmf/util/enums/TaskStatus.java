package com.ndrmf.util.enums;

public enum TaskStatus {
	PENDING("Pending"),
	COMPLETED("Completed"),
	UNSATISFACTORY("Unsatisfactory"),
	PARTIAL_UNSATISFACTORY("Partial Satisfactory"),
	SATISFACTORY("Satisfactory"),
	ASSIGNED("Assigned");
	
	private final String persistenceValue;
	
	private TaskStatus(String value) {
		this.persistenceValue = value;
	}
	
	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}
