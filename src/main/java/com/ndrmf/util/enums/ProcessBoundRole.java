package com.ndrmf.util.enums;

public enum ProcessBoundRole {
	PROCESS_OWNER("PROCESS OWNER"), SME("SME");
	
	private final String persistenceValue;
	
	private ProcessBoundRole(String value) {
		this.persistenceValue = value;
	}

	public String getPersistenceValue() {
		return persistenceValue;
	}
}
