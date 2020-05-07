package com.ndrmf.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ndrmf.event.QualificationCreatedEvent;

@Component
public class AccreditationEventListener {
	Logger logger = LoggerFactory.getLogger(AccreditationEventListener.class);
	
	@Async
	@EventListener
	public void handleQualificationCreatedEvent(QualificationCreatedEvent event) {
		logger.debug("Qualification Created Event Occurred.");
	}
}
