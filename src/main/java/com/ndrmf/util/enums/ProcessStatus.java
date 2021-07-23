package com.ndrmf.util.enums;

public enum ProcessStatus {
	DRAFT("Draft"),
	UNDER_REVIEW("Under Review"),
	APPROVED("Approved"),
	CONDITIONAL_APPROVED("Conditional Approved"),
	DEFERRED("Deferred"),
	REJECTED("Rejected"),
	NOT_INITIATED("Not Initiated"),
	REASSIGNED("Re Assigned"),
	PENDING("Pending"),
	REVIEW_PENDING("Review Pending"),
	REVIEW_COMPLETED("Review Completed"),
	COMPLETED("Completed"),
	PRELIMINARY_APPRAISAL("Preliminary Appraisal"),
	EXTENDED_APPRAISAL("Extended Appraisal"),
	TAC_MEETING("TAC Meeting"),
	RMC_MEETING("RMC Meeting"),
	BOD_MEETING("BOD Meeting"),
	OFFER_LETTER("Offer Letter"),
	MARKED_TO_GM("Marked to GM"),
	MARKED_TO_CEO("Marked to CEO"),
	GIA("GIA"),
	GIA_CHECKLIST("Checklist to FIP"),
	UPLOAD_PC1("Upload PC1"),
	UPLOAD_PDRMC("Upload PDRMC Mins"),
	GRANT_DISBURSMENT("Grant Disbursment"),
	BLOCKED("Blocked"),
	ACCREDITATION_QUESTIONNAIRE("Accreditation Quetionnaire"),
	DISBURSEMENT("Grant Disbursment"),
	MARKED_TO_PO("Marked To PO"),
	ELIGIBILITY("Eligibility"),
	PROJECT_PROPOSAL("Project Proposal"),
	QPR("QPR"),
	QUALIFICATION("Qualification"),
	QPR_TO_DONOR("Qpr To Donor"),
	SUB_PROJECT_DOCUMENT("Sub Project Doncument");

	private final String persistenceValue;
	
	private ProcessStatus(String value) {
		this.persistenceValue = value;
	}
	
	public String getPersistenceValue() {
		return this.persistenceValue;
	}
}
