package com.ndrmf.util.enums;

public enum ProcessStatus {
	DRAFT("Draft"),
	UNDER_REVIEW("Under Review"),
	APPROVED("Approved"),
	DEFERRED("Deferred"),
	REJECTED("Rejected"),
	NOT_INITIATED("Not Initiated"),
	REASSIGNED("Re Assigned"),
	PENDING("Pending"),
	COMPLETED("Completed"),
	PRELIMINARY_APPRAISAL("Preliminary Appraisal"),
	EXTENDED_APPRAISAL("Extended Appraisal"),
	TAC_MEETING("TAC Meeting"),
	RMC_MEETING("RMC Meeting"),
	BOD_MEETING("BOD Meeting"),
	OFFER_LETTER("Offer Letter");
	
	private final String persistenceValue;
	
	private ProcessStatus(String value) {
		this.persistenceValue = value;
	}
	
	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}
