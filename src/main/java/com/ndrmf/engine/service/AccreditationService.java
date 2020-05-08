package com.ndrmf.engine.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.AccreditationStatusItem;
import com.ndrmf.engine.dto.EligibilityListItem;
import com.ndrmf.engine.dto.EligibilityRequest;
import com.ndrmf.engine.dto.QualificationListItem;
import com.ndrmf.engine.dto.QualificationRequest;
import com.ndrmf.engine.model.Eligibility;
import com.ndrmf.engine.model.Qualification;
import com.ndrmf.engine.model.QualificationSection;
import com.ndrmf.engine.repository.EligibilityRepository;
import com.ndrmf.engine.repository.QualificationRepository;
import com.ndrmf.event.EligibilityApprovedEvent;
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
	
	@PersistenceContext private EntityManager em;
	
	public void addEligibility(UUID initiatedByUserId, EligibilityRequest body) {
		Set<String> constraintStatuses = new HashSet<>(); 
		constraintStatuses.add(ProcessStatus.DRAFT.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.APPROVED.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		
		int existingRequests = eligbiligyRepo.checkCountForUserWithStatuses(initiatedByUserId, constraintStatuses);
		
		if(existingRequests > 0) {
			throw new ValidationException("A request for this username already exists which is either APPROVED, UNDER REVIEW or in DRAFT Status");
		}
		
		Eligibility elig = new Eligibility();
		
		elig.setInitiatedBy(userRepo.getOne(initiatedByUserId));
		com.ndrmf.setting.model.ProcessType processMeta = processTypeRepo.findById(ProcessType.ELIGIBILITY.name()).get();
		
		elig.setProcessOwner(processMeta.getOwner());
		elig.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		elig.setTemplate(body.getTemplate());
		elig.setData(body.getData());
		
		eligbiligyRepo.save(elig);
	}
	
	public List<EligibilityListItem> getEligibilityRequests(UUID userId, ProcessStatus status) {
		List<Eligibility> eligs;
		
		if(status == null) {
			eligs = eligbiligyRepo.findAllRequestsForOwnerOrInitiator(userId);
		}
		else {
			eligs = eligbiligyRepo.findRequestsForOwnerOrInitiatorByStatus(userId, status.getPersistenceValue());	
		}
		
		List<EligibilityListItem> dtos = eligs.stream()
				.map(e -> new EligibilityListItem(e.getId(), e.getInitiatedBy().getFullName(), e.getCreatedDate(), e.getStatus()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	public List<QualificationListItem> getQualificationRequests(UUID ownerUserId, ProcessStatus status){
		List<Qualification> quals;
		
		if(status == null) {
			quals = qualificationRepo.findAllRequestsForOwnerOrInitiator(ownerUserId);
		}
		else {
			quals = qualificationRepo.findRequestsForOwnerOrInitiatorByStatus(ownerUserId, status.getPersistenceValue());
		}
		
		List<QualificationListItem> dtos = quals.stream()
				.map(q -> new QualificationListItem(q.getId(), q.getInitiatedBy().getFullName(), q.getCreatedDate(), q.getStatus()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	@Transactional
	public void addQualification(UUID initiatedByUseId, QualificationRequest body, FormAction action) {
		boolean areAllUnique = body.getSections().stream().map(s -> s.getId()).allMatch(new HashSet<>()::add);
		
		if(!areAllUnique) {
			throw new ValidationException("A Section ID cannot appear twice in a form");
		}
		
		Set<String> constraintStatuses = new HashSet<>(); 
		constraintStatuses.add(ProcessStatus.DRAFT.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.APPROVED.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		
		int existingRequests = qualificationRepo.checkCountForUserWithStatuses(initiatedByUseId, constraintStatuses);
		
		if(existingRequests > 0) {
			throw new ValidationException("A request for this username already exists which is either APPROVED, UNDER REVIEW or in DRAFT Status");
		}
		
		final Qualification q = new Qualification();
		
		q.setInitiatedBy(userRepo.getOne(initiatedByUseId));
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
	
	public void approveEligibilityRequest(UUID id, UUID approverUserId) {
		Eligibility elig = eligbiligyRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		if(!elig.getProcessOwner().getId().equals(approverUserId)) {
			throw new ValidationException("You cannot approve this request. Authorized user is: "+elig.getProcessOwner().getFullName());
		}
		
		if(!elig.getStatus().equals(ProcessStatus.UNDER_REVIEW.getPersistenceValue())){
			throw new ValidationException("Cannot approve request with status: "+elig.getStatus());
		}
		
		elig.setStatus(ProcessStatus.APPROVED.getPersistenceValue());
		
		elig = eligbiligyRepo.save(elig);
		
		eventPublisher.publishEvent(new EligibilityApprovedEvent(this, elig));
		
	}
	
	public AccreditationStatusItem getAccreditationStatus(UUID userId) {
		final String rawSql = "select er.status as eligibility, qs.status as qualification" + 
				" from eligibility_requests er" + 
				" left join qualifications qs on qs.initiator_user_id = er.initiator_user_id" + 
				" where er.initiator_user_id = :userId";
		
		Tuple result;
		
		try {
			result = (Tuple) em.createNativeQuery(rawSql, Tuple.class)
					.setParameter("userId", userId)
					.getSingleResult();
		}
		catch(NoResultException ex) {
			return new AccreditationStatusItem(false, ProcessStatus.NOT_INITIATED.getPersistenceValue(), ProcessStatus.NOT_INITIATED.getPersistenceValue());	
		}
		
		if(result.get("eligibility", String.class).equals(ProcessStatus.APPROVED.getPersistenceValue())
				&& result.get("qualification", String.class).equals(ProcessStatus.APPROVED.getPersistenceValue())){
			
			return new AccreditationStatusItem(true, result.get("eligibility", String.class), result.get("qualification", String.class));	
		}
		else {
			return new AccreditationStatusItem(false, result.get("eligibility", String.class), result.get("qualification", String.class));
		}
	}
	
	private User getProcessOwnerForProcess(ProcessType processType) {
		com.ndrmf.setting.model.ProcessType processMeta =
				processTypeRepo.findById(processType.name())
				.orElseThrow(() -> new RuntimeException("Invalid Process Type"));
		
		return processMeta.getOwner();
	}
}