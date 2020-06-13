package com.ndrmf.engine.service;

import java.util.ArrayList;
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

import com.ndrmf.engine.dto.AccreditationQuestionairreItem;
import com.ndrmf.engine.dto.AccreditationQuestionairreListItem;
import com.ndrmf.engine.dto.AccreditationStatusItem;
import com.ndrmf.engine.dto.AddQualificationTaskRequest;
import com.ndrmf.engine.dto.EligibilityItem;
import com.ndrmf.engine.dto.EligibilityListItem;
import com.ndrmf.engine.dto.EligibilityRequest;
import com.ndrmf.engine.dto.QualificationItem;
import com.ndrmf.engine.dto.QualificationListItem;
import com.ndrmf.engine.dto.QualificationSectionRequest;
import com.ndrmf.engine.dto.ReassignQualificationRequest;
import com.ndrmf.engine.dto.SectionItem;
import com.ndrmf.engine.dto.SubmitAccreditationQuestionairreRequest;
import com.ndrmf.engine.model.AccreditationQuestionairre;
import com.ndrmf.engine.model.Eligibility;
import com.ndrmf.engine.model.Qualification;
import com.ndrmf.engine.model.QualificationSection;
import com.ndrmf.engine.model.QualificationSectionReview;
import com.ndrmf.engine.model.QualificationTask;
import com.ndrmf.engine.repository.AccreditationQuestionairreRepository;
import com.ndrmf.engine.repository.EligibilityRepository;
import com.ndrmf.engine.repository.QualificationRepository;
import com.ndrmf.engine.repository.QualificationSectionRepository;
import com.ndrmf.engine.repository.QualificationTaskRepository;
import com.ndrmf.event.EligibilityApprovedEvent;
import com.ndrmf.event.QualificationCreatedEvent;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;
import com.ndrmf.util.enums.ReassignmentStatus;
import com.ndrmf.util.enums.ReviewStatus;
import com.ndrmf.util.enums.TaskStatus;

@Service
public class AccreditationService {
	@Autowired private UserRepository userRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private EligibilityRepository eligbiligyRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private QualificationRepository qualificationRepo;
	@Autowired private ApplicationEventPublisher eventPublisher;
	@Autowired private QualificationTaskRepository qtaskRepo;
	@Autowired private QualificationSectionRepository qsectionRepo;
	@Autowired private AccreditationQuestionairreRepository questionairreRepo;
	
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
	
	public EligibilityItem getEligibilityRequest(UUID id) {
		Eligibility elig = eligbiligyRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		EligibilityItem dto = new EligibilityItem();
		
		dto.setData(elig.getData());
		dto.setInitiatedBy(new UserLookupItem(elig.getInitiatedBy().getId(), elig.getInitiatedBy().getFullName()));
		dto.setProcessOwner(new UserLookupItem(elig.getProcessOwner().getId(), elig.getProcessOwner().getFullName()));
		dto.setStatus(elig.getStatus());
		dto.setTemplate(elig.getTemplate());
		dto.setSubmittedAt(elig.getCreatedDate());
		
		return dto;
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
	
	public QualificationItem getQualificationRequest(UUID id, UUID userId) {
		Qualification q = qualificationRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		QualificationItem dto = new QualificationItem();
		
		dto.setInitiatedBy(new UserLookupItem(q.getInitiatedBy().getId(), q.getInitiatedBy().getFullName()));
		dto.setOwner(q.getProcessOwner().getId().equals(userId));
		dto.setProcessOwner(new UserLookupItem(q.getProcessOwner().getId(), q.getProcessOwner().getFullName()));
		dto.setStatus(q.getStatus());
		dto.setSubmittedAt(q.getCreatedDate());
		
		if(q.getInitiatedBy().getId().equals(userId)) {
			List<QualificationTask> reassignmentComments = 
					qtaskRepo.findAllTasksForAssigneeAndRequest(userId, id);
			
			if(reassignmentComments != null && reassignmentComments.size() > 0) {
				QualificationTask lastTask = reassignmentComments.get(reassignmentComments.size() - 1);
				
				TaskItem ti = new TaskItem();
				ti.setComments(lastTask.getComments());
				ti.setEndDate(lastTask.getEndDate());
				ti.setStartDate(lastTask.getStartDate());
				ti.setStatus(lastTask.getStatus());
				
				dto.setReassignmentTask(ti);
			}
		}
		
		q.getSections().forEach(qs -> {
			SectionItem section = new SectionItem();
			
			section.setAssigned(qs.getSme().getId().equals(userId));
			section.setData(qs.getData());
			section.setId(qs.getId());
			section.setName(qs.getName());
			section.setPassingScore(qs.getPassingScore());
			section.setSme(new UserLookupItem(qs.getSme().getId(), qs.getSme().getFullName()));
			section.setTemplate(qs.getTemplate());
			section.setTemplateType(qs.getTemplateType());
			section.setTotalScore(qs.getTotalScore());
			
			section.setReviewStatus(qs.getReviewStatus());
			
			section.setReassignmentStatus(qs.getReassignmentStatus());
			
			
			if(qs.getReviews() != null && qs.getReviews().size() > 0) {
				QualificationSectionReview latestReview = 
						qs.getReviews().get(qs.getReviews().size() - 1);
				
				
				section.setReview(latestReview.getCreatedDate(),
						latestReview.getControlWiseComments(),
						latestReview.getRating(),
						latestReview.getStatus(),
						latestReview.getComments());
			
				
				qs.getReviews().forEach(r -> {
					section.addReviewHistory(r.getCreatedDate(), r.getControlWiseComments(), r.getRating(), r.getStatus(), r.getComments());
				});
			}
			
			dto.addSection(section);
		});
		
		return dto;
	}
	
	public UUID commenceQualification(UUID initiatorUserId) {
		Set<String> constraintStatuses = new HashSet<>(); 
		constraintStatuses.add(ProcessStatus.DRAFT.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.APPROVED.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.REASSIGNED.getPersistenceValue());
		
		int existingRequests = qualificationRepo.checkCountForUserWithStatuses(initiatorUserId, constraintStatuses);
		
		if(existingRequests > 0) {
			throw new ValidationException("A request for this username already exists which is either APPROVED, UNDER REVIEW or in DRAFT Status");
		}
		
		Qualification q = new Qualification();
		
		q.setInitiatedBy(userRepo.getOne(initiatorUserId));
		q.setProcessOwner(this.getProcessOwnerForProcess(ProcessType.QUALIFICATION));
		q.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		
		List<SectionTemplate> sts = sectionTemplateRepo.findTemplatesForProcessType(ProcessType.QUALIFICATION.toString());
		
		for(SectionTemplate st: sts) {
			QualificationSection qs = new QualificationSection();
			qs.setName(st.getSection().getName());
			qs.setPassingScore(st.getPassingScore());
			qs.setTotalScore(st.getTotalScore());
			qs.setTemplateType(st.getTemplateType());
			qs.setTemplate(st.getTemplate());
			qs.setSme(st.getSection().getSme());
			qs.setSectionRef(st.getSection());
			
			q.addSection(qs);
		}
		
		q = qualificationRepo.save(q);
		
		return q.getId();
	}
	
	public void addQualificationSection(UUID initiatedByUseId, UUID requestId, QualificationSectionRequest body, FormAction action) {
		Qualification q = qualificationRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		if(!q.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())
				&& !q.getStatus().equals(ProcessStatus.REASSIGNED.getPersistenceValue())) {
			throw new ValidationException("Request is already: " + q.getStatus());
		}
		
		QualificationSection section = q.getSections().stream().filter(s -> s.getId().equals(body.getId())).findAny().orElse(null);
		
		if(section == null) {
			throw new ValidationException("Invalid ID");
		}
		
		section.setData(body.getData());
		
		if(action == FormAction.SAVE) {
			q.setStatus(ProcessStatus.DRAFT.getPersistenceValue());	
		}
		else if(action == FormAction.SUBMIT) {
			q.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		}
		else {
			q.setStatus(ProcessStatus.DRAFT.getPersistenceValue()); //For lack of a better default
		}
		
		if(section.getReassignmentStatus() != null && section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
			section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
		}
		
		qualificationRepo.save(q);
		
		if(action == FormAction.SUBMIT) {
			eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));	
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
	
	public AccreditationStatusItem getAccreditationStatus(UUID currentUserId, List<String> roles) {
		if(roles.contains(SystemRoles.ORG_GOVT)) {
			AccreditationQuestionairre q = questionairreRepo.findByForUser(currentUserId)
					.orElse(null);
			
			if(q != null) {
				if(q.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue())) {
					return new AccreditationStatusItem(true, "Approved", "Approved", false);	
				}
				else {
					return new AccreditationStatusItem(false, null, null, false);
				}
			}
		}
		final String rawSql = "select er.status as eligibility, qs.status as qualification" + 
				" from eligibility_requests er" + 
				" left join qualifications qs on qs.initiator_user_id = er.initiator_user_id" + 
				" where er.initiator_user_id = :userId";
		
		Tuple result;
		
		try {
			result = (Tuple) em.createNativeQuery(rawSql, Tuple.class)
					.setParameter("userId", currentUserId)
					.getSingleResult();
		}
		catch(NoResultException ex) {
			return new AccreditationStatusItem(false, ProcessStatus.NOT_INITIATED.getPersistenceValue(), ProcessStatus.NOT_INITIATED.getPersistenceValue(), true);	
		}
		
		String qualificationStatus = result.get("qualification", String.class);
		if(qualificationStatus == null) {
			qualificationStatus = ProcessStatus.NOT_INITIATED.getPersistenceValue();
		}
		
		if(result.get("eligibility", String.class) != null && result.get("eligibility", String.class).equals(ProcessStatus.APPROVED.getPersistenceValue())
				&& result.get("qualification", String.class) != null && result.get("qualification", String.class).equals(ProcessStatus.APPROVED.getPersistenceValue())){
			return new AccreditationStatusItem(true, result.get("eligibility", String.class), qualificationStatus, false);	
		}
		else {
			return new AccreditationStatusItem(false, result.get("eligibility", String.class), qualificationStatus, true);
		}
	}
	
	@Transactional
	public void addQualificationTask(UUID sectionId, UUID currentUserId, AddQualificationTaskRequest body) {
		QualificationSection section = qsectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		if(!section.getQualifcationRef().getProcessOwner().getId().equals(currentUserId)) {
			throw new ValidationException("Only Process Owner for this process can add tasks. Authorized user is: "+ section.getQualifcationRef().getProcessOwner().getFullName());
		}
		
		QualificationTask task = new QualificationTask();
		
		task.setStartDate(body.getStartDate());
		task.setEndDate(body.getEndDate());
		task.setComments(body.getComments());
		
		task.setSection(section);
		task.setAssignee(section.getSme());
		
		task.setStatus(TaskStatus.PENDING.getPersistenceValue());
		
		section.setReviewStatus(ReviewStatus.PENDING.getPersistenceValue());
		
		qtaskRepo.save(task);
		
		//TODO - trigger notification event
	}
	
	private User getProcessOwnerForProcess(ProcessType processType) {
		com.ndrmf.setting.model.ProcessType processMeta =
				processTypeRepo.findById(processType.name())
				.orElseThrow(() -> new RuntimeException("Invalid Process Type"));
		
		return processMeta.getOwner();
	}
	
	public void updateQualificationStatus(UUID id, ProcessStatus status) {
		Qualification q = qualificationRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		
		q.setStatus(status.getPersistenceValue());
		
		qualificationRepo.save(q);
		
		//TODO - raise event, notify FIP, update accreditation status
	}
	
	@Transactional
	public void reassignQualificationRequest(UUID id, UUID userId, ReassignQualificationRequest body) {
		Qualification q = qualificationRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		if(!q.getProcessOwner().getId().equals(userId)) {
			throw new ValidationException("Cannot re-assign. Authorized user is: " + q.getProcessOwner().getFullName());
		}
		
		QualificationTask task = new QualificationTask();
		task.setStartDate(body.getStartDate());
		task.setEndDate(body.getEndDate());
		task.setComments(body.getComments());
		task.setAssignee(q.getInitiatedBy());
		task.setStatus(TaskStatus.PENDING.getPersistenceValue());
		task.setQualification(q);
		
		qtaskRepo.save(task);
		
		body.getSectionIds().forEach(sid -> {
			QualificationSection section = q.getSections()
					.stream().filter(s -> s.getId().equals(sid))
					.findAny()
					.orElseThrow(() -> new ValidationException("Invalid section ID"));
			
			section.setReassignmentStatus(ReassignmentStatus.PENDING.getPersistenceValue());
		});
		
		q.setStatus(ProcessStatus.REASSIGNED.getPersistenceValue());
		
		//TODO: Raise event, inform FIP about reassignment
	}
	
	public List<AccreditationQuestionairreListItem> getAllQuestionairreRequests(UUID userId) {
		List<AccreditationQuestionairre> qs = questionairreRepo.findAllByAssignee(userId);
		List<AccreditationQuestionairreListItem> dtos = new ArrayList<>();
		
		qs.forEach(q -> {
			AccreditationQuestionairreListItem item = new AccreditationQuestionairreListItem();
			item.setId(q.getId());
			item.setAssigned(userId.equals(q.getAssignee().getId()));
			item.setForUser(new UserLookupItem(q.getForUser().getId(), q.getForUser().getFullName()));
			item.setAssignee(new UserLookupItem(q.getAssignee().getId(), q.getAssignee().getFullName()));
			item.setStatus(q.getStatus());
			
			dtos.add(item);
		});
		
		return dtos;
	}
	
	public AccreditationQuestionairreItem getQuestionairreRequest(UUID userId, UUID id) {
		AccreditationQuestionairre q = questionairreRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid Request ID"));
		
		AccreditationQuestionairreItem dto = new AccreditationQuestionairreItem();
		dto.setAssigned(userId.equals(q.getAssignee().getId()));
		dto.setForUser(new UserLookupItem(q.getForUser().getId(), q.getForUser().getFullName()));
		dto.setAssignee(new UserLookupItem(q.getAssignee().getId(), q.getAssignee().getFullName()));
		
		dto.setStatus(q.getStatus());
		dto.setTemplate(q.getTemplate());
		dto.setData(q.getData());
		
		return dto;
	}
	
	public List<AccreditationQuestionairreListItem> getPendingQuestionairres(UUID userId) {
		List<AccreditationQuestionairre> qs = questionairreRepo.findAllByAssigneeAndStatus(userId, ProcessStatus.PENDING.getPersistenceValue());
		List<AccreditationQuestionairreListItem> dtos = new ArrayList<>();
		
		qs.forEach(q -> {
			AccreditationQuestionairreListItem item = new AccreditationQuestionairreListItem();
			item.setId(q.getId());
			item.setAssigned(userId.equals(q.getAssignee().getId()));
			item.setForUser(new UserLookupItem(q.getForUser().getId(), q.getForUser().getFullName()));
			item.setAssignee(new UserLookupItem(q.getAssignee().getId(), q.getAssignee().getFullName()));
			
			dtos.add(item);
		});
		
		return dtos;
	}
	
	@Transactional
	public void submitAccreditationQuestionairre(UUID userId, UUID id, SubmitAccreditationQuestionairreRequest body) {
		AccreditationQuestionairre q = questionairreRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid Request ID"));
		
		if(!userId.equals(q.getAssignee().getId())) {
			throw new ValidationException("Unauthorized. Authorized User is: " + q.getAssignee().getFullName());
		}
		
		q.setTemplate(body.getTemplate());
		q.setData(body.getData());
		q.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
	}
}