package com.ndrmf.util.enums;

public enum ComplaintStatus {

	INITIATED("Initiated"), 
	MARKED_TO_FOCAL_PERSON("Marked to Focal Person"),
	MARKED_TO_CONCERNED_PERSON("Marked to Concerned Person"),
	MARKED_TO_GRC("Marked to GRC"),
	MARKED_TO_COMPLAINANT("Marked to Complainant"),
	UNDER_REVIEW("Under Review"),
	APPROVED_BY_GRC("Approved by GRC"),
	DEFERRED("Deferred"),
	REJECTED("Rejected"),
	WITHDRAWN("Withdrawn"),
	REASSIGNED("Re Assigned"),
	PENDING("Pending"),
	REVIEW_PENDING("Review Pending"),
	COMPLETED("Completed"),
	IN_PROCESS("In Process");

	private final String persistenceValue;

	private ComplaintStatus(String value) {
		this.persistenceValue = value;
	}

	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}
