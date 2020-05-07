package com.ndrmf.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ndrmf.event.QualificationCreatedEvent;
import com.ndrmf.notification.service.NotificationService;

@Component
public class AccreditationEventListener {
	private final Logger logger = LoggerFactory.getLogger(AccreditationEventListener.class);
	
	@Autowired NotificationService notiService;
	
	@Async
	@EventListener
	public void handleQualificationCreatedEvent(QualificationCreatedEvent event) {
		logger.debug("Qualification Created Event Occurred.");
		
		notiService.addNotification(event.getQualification().getProcessOwner().getId(), "Qualification Submitted. Please Review.", "Qualification Submitted. Please Review.");
		
		event.getQualification().getSections().forEach(s -> {
			notiService.addNotification(s.getSme().getId(), String.format("Qualification Submitted. Please review section '%s'.", s.getName()), "Please Review");
		});
	}
}
