package com.ndrmf.engine.dto;

import com.ndrmf.util.enums.ProcessStatus;

public class AccreditationStatusItem {
	private final boolean accredited;
	private final String eligibilityStatus;
	private final String qualificationStatus;
	
	public AccreditationStatusItem(boolean accredited, String eligibilityStatus, String qualificationStatus) {
		this.accredited = accredited;
		this.eligibilityStatus = eligibilityStatus;
		this.qualificationStatus = qualificationStatus;
	}

	public boolean isAccredited() {
		return accredited;
	}

	public String getEligibilityStatus() {
		return eligibilityStatus;
	}

	public String getQualificationStatus() {
		return qualificationStatus == null ? ProcessStatus.NOT_INITIATED.getPersistenceValue()
				: qualificationStatus;
	}
}
