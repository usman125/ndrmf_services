package com.ndrmf.event;

import org.springframework.context.ApplicationEvent;

import com.ndrmf.engine.model.Eligibility;

@SuppressWarnings("serial")
public class EligibilityApprovedEvent extends ApplicationEvent{
	private final Eligibility eligibility;
	
	public EligibilityApprovedEvent(Object source, Eligibility eligibility) {
		super(source);
		
		this.eligibility = eligibility;
	}

	public Eligibility getEligibility() {
		return eligibility;
	}
}
