package com.ndrmf.event;

import org.springframework.context.ApplicationEvent;

import com.ndrmf.engine.model.Qualification;

@SuppressWarnings("serial")
public class QualificationCreatedEvent extends ApplicationEvent {
	private final Qualification qualification;
	
	public QualificationCreatedEvent(Object source, Qualification qualification) {
		super(source);
		
		this.qualification = qualification;
	}

	public Qualification getQualification() {
		return qualification;
	}
}
