package com.ndrmf.engine.service;


import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.model.*;
import com.ndrmf.engine.repository.*;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.dto.ThematicAreaItem;
import com.ndrmf.setting.repository.DesignationRepository;
import com.ndrmf.user.dto.SmeLookupItem;
import com.ndrmf.user.dto.UserItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
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
	@Autowired private FIPThematicAreaRepository fipThematicAreaRepo;
	@Autowired private DesignationRepository designationRepository;
	@Autowired private NotificationService notificationService;
	
	@PersistenceContext private EntityManager em;
	
	public void addEligibility(AuthPrincipal initiatedByUser, EligibilityRequest body) {
		Set<String> constraintStatuses = new HashSet<>(); 
		constraintStatuses.add(ProcessStatus.DRAFT.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.APPROVED.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		
		int existingRequests = eligbiligyRepo.checkCountForUserWithStatuses(initiatedByUser.getUserId(), constraintStatuses);
		
		if(existingRequests > 0) {
			throw new ValidationException("A request for this username already exists which is either APPROVED, UNDER REVIEW or in DRAFT Status");
		}
		
		Eligibility elig = new Eligibility();
		
		elig.setInitiatedBy(userRepo.getOne(initiatedByUser.getUserId()));
		com.ndrmf.setting.model.ProcessType processMeta = processTypeRepo.findById(ProcessType.ELIGIBILITY.name()).get();
		
		elig.setProcessOwner(processMeta.getOwner());
		elig.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		elig.setTemplate(body.getTemplate());
		elig.setData(body.getData());
		
		Eligibility eligiRequest = eligbiligyRepo.save(elig);

		try {
			notificationService.sendPlainTextEmail(
					processMeta.getOwner().getEmail(),
					processMeta.getOwner().getFullName(),
					"New Eligibility Request Submitted",
					processMeta.getOwner().getFullName() +
							" " +
							initiatedByUser.getFullName() +
							", has submitted a new eligibility request at NDRMF please visit " +
							"http://ndrmfdev.herokuapp.com/eligibility-requests/"+eligiRequest.getId()
							+ " to view the request."
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void updateEligibility(UUID initiatedByUserId, UUID eligId, EligibilityRequest body) {
		Eligibility elig = eligbiligyRepo.findById(eligId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		//elig.setInitiatedBy(userRepo.getOne(initiatedByUserId));
		//com.ndrmf.setting.model.ProcessType processMeta = processTypeRepo.findById(ProcessType.ELIGIBILITY.name()).get();
		
		//elig.setProcessOwner(processMeta.getOwner());
		elig.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		//elig.setTemplate(body.getTemplate());
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
				.map(e -> new EligibilityListItem(e.getId(), e.getInitiatedBy().getFullName(), e.getCreatedDate(), e.getStatus(),
						e.getComment()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	public EligibilityItem getEligibilityRequest(UUID id, UUID userId) {
		Eligibility elig = eligbiligyRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		List<FIPThematicArea> fiptalist;

		if (elig.getInitiatedBy() == null){
			fiptalist = fipThematicAreaRepo.getAllThematicAreasForUser(userId);
		}else{
			fiptalist = fipThematicAreaRepo.getAllThematicAreasForUser(elig.getInitiatedBy().getId());
		}

		List<FipThematicAreasListItem> tadto = new ArrayList<>();

		fiptalist.forEach(item -> {
			FipThematicAreasListItem fiptalitem = new FipThematicAreasListItem();
			fiptalitem.setId(item.getId());
			fiptalitem.setFip(new UserLookupItem(item.getFip().getId(), item.getFip().getFullName()));

			ThematicAreaItem tai = new ThematicAreaItem();
			tai.setId(item.getThematicArea().getId());
			tai.setName(item.getThematicArea().getName());
			tai.setProcessOwner(
					new UserLookupItem(
							item.getThematicArea().getProcessOwner().getId(),
							item.getThematicArea().getProcessOwner().getFullName()
					)
			);
			fiptalitem.setThematicAreaItem(tai);
			fiptalitem.setExperience(item.getExperience());
			fiptalitem.setCounterpart(item.getCounterpart());

			tadto.add(fiptalitem);
		});
		
		EligibilityItem dto = new EligibilityItem();
		
		dto.setData(elig.getData());
		dto.setInitiatedBy(new UserLookupItem(elig.getInitiatedBy().getId(), elig.getInitiatedBy().getFullName()));
		dto.setProcessOwner(new UserLookupItem(elig.getProcessOwner().getId(), elig.getProcessOwner().getFullName()));
		dto.setStatus(elig.getStatus());
		dto.setTemplate(elig.getTemplate());
		dto.setSubmittedAt(elig.getCreatedDate());
		dto.setComment(elig.getComment());
		dto.setFipThematicAreasListItem(tadto);
		
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
				.map(q -> new QualificationListItem(q.getId(), q.getInitiatedBy().getFullName(), q.getCreatedDate(), q.getStatus(),
						q.getExpiryDate(), q.getSubStatus(), q.getComment()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	public EligPlusQual getQualificationRequest(UUID id, UUID userId) {
		Qualification q = qualificationRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		EligPlusQual epqDto = new EligPlusQual();
		
		List<Eligibility> elig = eligbiligyRepo.findAllRequestsForInitiator(q.getInitiatedBy().getId());
		List<EligibilityItem> edto = new ArrayList<EligibilityItem>();
		
		for (int i = 0; i < elig.size(); i++) {
			EligibilityItem singleEdto = new EligibilityItem();
			
			singleEdto.setData(elig.get(i).getData());
			singleEdto.setInitiatedBy(new UserLookupItem(elig.get(i).getInitiatedBy().getId(), elig.get(i).getInitiatedBy().getFullName()));
			singleEdto.setProcessOwner(new UserLookupItem(elig.get(i).getProcessOwner().getId(), elig.get(i).getProcessOwner().getFullName()));
			singleEdto.setStatus(elig.get(i).getStatus());
			singleEdto.setTemplate(elig.get(i).getTemplate());
			singleEdto.setSubmittedAt(elig.get(i).getCreatedDate());
			singleEdto.setComment(elig.get(i).getComment());
			edto.add(singleEdto);
		}
		
		
		
		QualificationItem qdto = new QualificationItem();
		
		qdto.setInitiatedBy(new UserLookupItem(q.getInitiatedBy().getId(), q.getInitiatedBy().getFullName()));
		qdto.setOwner(q.getProcessOwner().getId().equals(userId));
		qdto.setProcessOwner(new UserLookupItem(q.getProcessOwner().getId(), q.getProcessOwner().getFullName()));
		qdto.setStatus(q.getStatus());
		qdto.setSubmittedAt(q.getCreatedDate());
		qdto.setExpiryDate(q.getExpiryDate());
		qdto.setComment(q.getComment());
		qdto.setSubStatus(q.getSubStatus());
		qdto.setLastModified(q.getLastModifiedDate());
		qdto.setReviewUsers(q.getReportUsers());

//		if (q.getInitiatedBy().isAvailableAsJv() && q.getInitiatedBy().getJvUser() != null){
//			qdto.setJvUser(new UserLookupItem(
//					userRepo.findById(q.getInitiatedBy().getJvUserId()).get().getId(),
//					userRepo.findById(q.getInitiatedBy().getJvUserId()).get().getFullName())
//			);
//		}
		qdto.setAvailableAsJv(q.getInitiatedBy().isAvailableAsJv());
		qdto.setJvUser(q.getInitiatedBy().getJvUser());

		
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
				
				qdto.setReassignmentTask(ti);
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
			section.setReportSme(new SmeLookupItem(qs.getSme().getId(),
					qs.getSme().getFullName(), designationRepository.getOne(qs.getSme().getDesignation().getId()).getName()));
			section.setTemplate(qs.getTemplate());
			section.setTemplateType(qs.getTemplateType());
			section.setTotalScore(qs.getTotalScore());
			
			section.setReviewStatus(qs.getReviewStatus());
			
			section.setReassignmentStatus(qs.getReassignmentStatus());
			section.setOrderNum(qs.getSectionRef().getOrderNum());
			
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
			
			qdto.addSection(section);
		});

		List<FIPThematicArea> fiptalist = fipThematicAreaRepo.getAllThematicAreasForUser(q.getInitiatedBy().getId());
		List<FipThematicAreasListItem> tadto = new ArrayList<>();

		fiptalist.forEach(item -> {
			FipThematicAreasListItem fiptalitem = new FipThematicAreasListItem();
			fiptalitem.setId(item.getId());
			fiptalitem.setFip(new UserLookupItem(item.getFip().getId(), item.getFip().getFullName()));

			ThematicAreaItem tai = new ThematicAreaItem();
			tai.setId(item.getThematicArea().getId());
			tai.setName(item.getThematicArea().getName());
			tai.setProcessOwner(
					new UserLookupItem(
							item.getThematicArea().getProcessOwner().getId(),
							item.getThematicArea().getProcessOwner().getFullName()
					)
			);
			fiptalitem.setThematicAreaItem(tai);
			fiptalitem.setExperience(item.getExperience());
			fiptalitem.setCounterpart(item.getCounterpart());

			tadto.add(fiptalitem);
		});

		User userIfo = userRepo.findById(q.getInitiatedBy().getId()).orElseThrow(() -> new ValidationException("Invalid User ID"));

		UserItem uidto = new UserItem();

		uidto.setId(userIfo.getId());
		uidto.setUsername(userIfo.getUsername());
		uidto.setEmail(userIfo.getEmail());
		uidto.setFirstName(userIfo.getFirstName());
		uidto.setLastName(userIfo.getLastName());
		uidto.setEnabled(userIfo.isEnabled());
		uidto.setSAP(userIfo.isSAP());

		if(userIfo.getOrg() != null) {
			uidto.setOrgId(userIfo.getOrg().getId());
			uidto.setOrgName(userIfo.getOrg().getName());
		}

		List<Map<String, Object>> roles = new ArrayList<>();

		if(userIfo.getRoles() != null) {
			userIfo.getRoles().forEach(r -> {
				Map<String, Object> role = new HashMap<>();
				role.put("id", r.getId());
				role.put("name", r.getName());

				roles.add(role);
			});

			uidto.setRoles(roles);
		}

		uidto.setEntityName(userIfo.getEntityName());
		uidto.setEntityNature(userIfo.getEntityNature());
		uidto.setEntityType(userIfo.getEntityType());
		uidto.setLocation(userIfo.getLocation());
		uidto.setAddress(userIfo.getAddress());
		uidto.setProvince(userIfo.getProvince());
		uidto.setOtherAddress(userIfo.getOtherAddress());
		uidto.setOtherAccreditation(userIfo.getOtherAccreditation());
		
		epqDto.setQualItem(qdto);
		epqDto.setEligItem(edto);
		epqDto.setThematicAreasListItems(tadto);
		epqDto.setUserInfo(uidto);

		return epqDto;
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
		
//		if(!elig.getStatus().equals(ProcessStatus.UNDER_REVIEW.getPersistenceValue())){
//			throw new ValidationException("Cannot approve request with status: "+elig.getStatus());
//		}
		
		elig.setStatus(ProcessStatus.APPROVED.getPersistenceValue());
		elig = eligbiligyRepo.save(elig);
		eventPublisher.publishEvent(new EligibilityApprovedEvent(this, elig));

		try {
			notificationService.sendPlainTextEmail(
					elig.getInitiatedBy().getEmail(),
					elig.getInitiatedBy().getFullName(),
					"Eligibility Request at NDRMF has been " + ProcessStatus.APPROVED.getPersistenceValue(),
					elig.getInitiatedBy().getFullName() +
					", your eligibility request at NDRMF has been " + ProcessStatus.APPROVED.getPersistenceValue() +
					"please visit http://ndrmfdev.herokuapp.com/eligibility-requests/"+elig.getId()
					+ " to view the request."
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void rejectEligibilityRequest(UUID id, UUID approverUserId, Comment body) {
		Eligibility elig = eligbiligyRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		if(!elig.getProcessOwner().getId().equals(approverUserId)) {
			throw new ValidationException("You cannot approve this request. Authorized user is: "+elig.getProcessOwner().getFullName());
		}
		
//		if(!elig.getStatus().equals(ProcessStatus.UNDER_REVIEW.getPersistenceValue())){
//			throw new ValidationException("Cannot approve request with status: "+elig.getStatus());
//		}
		
		elig.setStatus(ProcessStatus.REJECTED.getPersistenceValue());
		elig.setComment(body.getComment());
		
		elig = eligbiligyRepo.save(elig);
		
		eventPublisher.publishEvent(new EligibilityApprovedEvent(this, elig));

		try {
			notificationService.sendPlainTextEmail(
					elig.getInitiatedBy().getEmail(),
					elig.getInitiatedBy().getFullName(),
					"Eligibility Request at NDRMF has been " + ProcessStatus.REJECTED.getPersistenceValue(),
					elig.getInitiatedBy().getFullName() +
					", your eligibility request at NDRMF has been " + ProcessStatus.REJECTED.getPersistenceValue() +
					"please visit http://ndrmfdev.herokuapp.com/eligibility-requests/"+elig.getId()
					+ " to view the request."
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Object getAccreditationStatus(UUID currentUserId, List<String> roles) {
		//AccreditationStatusItem was the return type
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

//		final String rawSql = "select er.status as eligibility, qs.status as qualification" +
//				" from eligibility_requests er" +
//				" left join qualifications qs on qs.initiator_user_id = er.initiator_user_id" +
//				" where er.initiator_user_id = :userId order by er.last_modified_date desc

		Tuple result;
		List<Tuple> temp;
		
		try {
			temp = em.createNativeQuery(rawSql, Tuple.class)
					.setParameter("userId", currentUserId).getResultList();
					//.getSingleResult();
		}
		catch(NoResultException ex) {
			return new AccreditationStatusItem(false, ProcessStatus.NOT_INITIATED.getPersistenceValue(), ProcessStatus.NOT_INITIATED.getPersistenceValue(), true);	
		}
		if (temp.isEmpty()) {
			return new AccreditationStatusItem(false, ProcessStatus.NOT_INITIATED.getPersistenceValue(), ProcessStatus.NOT_INITIATED.getPersistenceValue(), true);
		}
		
		//return temp.get(temp.size()-1);
		result = temp.get(temp.size()-1);
		
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
	
	public void updateQualificationStatus(UUID id, QualificationItem body) {
		//ProcessStatus status, Date expiryDate, String comment, ProcessStatus subStatus) {
		Qualification q = qualificationRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		if (Objects.equal(body.getStatus(), ProcessStatus.MARKED_TO_GM.getPersistenceValue()))
		{
			q.setMarkedTo(ProcessStatus.MARKED_TO_GM);
		}
		else if (Objects.equal(body.getStatus(), ProcessStatus.MARKED_TO_CEO.getPersistenceValue()))
		{
			q.setMarkedTo(ProcessStatus.MARKED_TO_CEO);
		}else {
			q.setStatus(ProcessStatus.valueOf( body.getStatus()).getPersistenceValue());
		}
		q.setMarkedTo(body.getMarkedTo());
		q.setExpiryDate(body.getExpiryDate());
		q.setComment(body.getComment());
		q.setSubStatus(body.getSubStatus());
		
		qualificationRepo.save(q);
		
		//TODO - raise event, notify FIP, update accreditation status
	}

	public void addQualificationReviewUsers(UUID id, QualificationReviewUsersRequest body) {
		//ProcessStatus status, Date expiryDate, String comment, ProcessStatus subStatus) {
		Qualification q = qualificationRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		q.setReportUsers(body.getReviewUsers());
//		q.setMarkedTo(body.getMarkedTo());
//		q.setExpiryDate(body.getExpiryDate());
//		q.setComment(body.getComment());
//		q.setSubStatus(body.getSubStatus());

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

			item.setJvUser(q.getForUser().getJvUser());
			item.setJv(q.getForUser().isAvailableAsJv());

			item.setStatus(q.getStatus());
			item.setData(q.getData());
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

		dto.setJvUser(q.getForUser().getJvUser());
		dto.setJv(q.getForUser().isAvailableAsJv());

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
			item.setStatus(q.getStatus());
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