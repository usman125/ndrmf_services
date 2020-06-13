package com.ndrmf.engine.dto;

import com.ndrmf.util.enums.ProcessStatus;

public class AccreditationStatusItem {
	private final boolean accredited;
	private final String eligibilityStatus;
	private final String qualificationStatus;
	private final boolean canInitiate;
	
	
	public AccreditationStatusItem(boolean accredited, String eligibilityStatus, String qualificationStatus, boolean canInitiate) {
		this.accredited = accredited;
		this.eligibilityStatus = eligibilityStatus;
		this.qualificationStatus = qualificationStatus;
		this.canInitiate = canInitiate;
	}

	public boolean isAccredited() {
		return accredited;
	}

	public String getEligibilityStatus() {
		return eligibilityStatus;
	}

	public String getQualificationStatus() {
		return qualificationStatus;
	}

	public boolean isCanInitiate() {
		return canInitiate;
	}
}
