package com.ndrmf.engine.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.EligibilityListItem;
import com.ndrmf.engine.dto.EligibilityRequest;
import com.ndrmf.engine.dto.QualificationRequest;
import com.ndrmf.engine.model.Eligibility;
import com.ndrmf.engine.model.Qualification;
import com.ndrmf.engine.model.QualificationSection;
import com.ndrmf.engine.repository.EligibilityRepository;
import com.ndrmf.engine.repository.QualificationRepository;
import com.ndrmf.event.QualificationCreatedEvent;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

@Service
public class AccreditationService {
	@Autowired private UserRepository userRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private EligibilityRepository eligbiligyRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private QualificationRepository qualificationRepo;
	@Autowired private ApplicationEventPublisher eventPublisher;
	
	public void addEligibility(String initiatedByUsername, EligibilityRequest body) {
		Set<String> constraintStatuses = new HashSet<>(); 
		constraintStatuses.add(ProcessStatus.DRAFT.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.APPROVED.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		
		int existingRequests = eligbiligyRepo.checkCountForUserWithStatuses(initiatedByUsername, constraintStatuses);
		
		if(existingRequests > 0) {
			throw new ValidationException("A request for this username already exists which is either APPROVED, UNDER REVIEW or in DRAFT Status");
		}
		
		Eligibility elig = new Eligibility();
		
		elig.setInitiatedBy(userRepo.findByUsername(initiatedByUsername));
		com.ndrmf.setting.model.ProcessType processMeta = processTypeRepo.findById(ProcessType.ELIGIBILITY.name()).get();
		
		elig.setProcessOwner(processMeta.getOwner());
		elig.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		elig.setTemplate(body.getTemplate());
		elig.setData(body.getData());
		
		eligbiligyRepo.save(elig);
	}
	
	public List<EligibilityListItem> getEligibilityRequests(String ownerUsername, ProcessStatus status) {
		List<Eligibility> eligs = Collections.emptyList();
		
		if(status == null) {
			eligs = eligbiligyRepo.findAllRequestsForOwner(ownerUsername);
		}
		else {
			eligs = eligbiligyRepo.findRequestsForOwnerByStatus(ownerUsername, status.getPersistenceValue());	
		}
		
		List<EligibilityListItem> dtos = eligs.stream()
				.map(e -> new EligibilityListItem(e.getId(), e.getProcessOwner().getFullName(), e.getCreatedDate(), e.getStatus()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	@Transactional
	public void addQualification(String initiatedByUsername, QualificationRequest body, FormAction action) {
		boolean areAllUnique = body.getSections().stream().map(s -> s.getId()).allMatch(new HashSet<>()::add);
		
		if(!areAllUnique) {
			throw new ValidationException("A Section ID cannot appear twice in a form");
		}
		
		
		final Qualification q = new Qualification();
		
		q.setInitiatedBy(userRepo.findByUsername(initiatedByUsername));
		q.setProcessOwner(this.getProcessOwnerForProcess(ProcessType.QUALIFICATION));
		
		if(action == FormAction.SAVE) {
			q.setStatus(ProcessStatus.DRAFT.getPersistenceValue());	
		}
		else if(action == FormAction.SUBMIT) {
			q.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		}
		else {
			q.setStatus(ProcessStatus.DRAFT.getPersistenceValue()); //For lack of a better default
		}
		
		body.getSections().forEach(s -> {
			SectionTemplate template =
					sectionTemplateRepo.findEnabledTemplateBySectionId(s.getId())
					.orElseThrow(() -> new ValidationException("Invalid Section ID: "+s.getId().toString()));
			
			QualificationSection qs = new QualificationSection();
			qs.setName(template.getSection().getName());
			qs.setPassingScore(template.getPassingScore());
			qs.setTotalScore(template.getTotalScore());
			qs.setTemplateType(template.getTemplateType());
			qs.setTemplate(template.getTemplate());
			qs.setData(s.getData());
			qs.setSme(template.getSection().getSme());
			
			q.addSection(qs);
			
		});
		
		Qualification persistedQual = qualificationRepo.save(q);
		
		if(action == FormAction.SUBMIT) {
			eventPublisher.publishEvent(new QualificationCreatedEvent(this, persistedQual));	
		}
	}
	
	private User getProcessOwnerForProcess(ProcessType processType) {
		com.ndrmf.setting.model.ProcessType processMeta =
				processTypeRepo.findById(processType.name())
				.orElseThrow(() -> new RuntimeException("Invalid Process Type"));
		
		return processMeta.getOwner();
	}
}