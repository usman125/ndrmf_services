package com.ndrmf.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.common.rFile;
import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.model.*;
import com.ndrmf.engine.repository.*;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.KeyValue;
import com.ndrmf.util.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Service
public class QprToDonorService {
	@Autowired
	private QprToDonorRepository qprToDonorRepo;
	@Autowired
	private SectionTemplateRepository sectionTemplateRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private QprToDonorTaskRepository qprtodnortaskRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private QprToDonorTaskRepository qprToDonorTaskRepo;
	@Autowired
	private QprToDonorSectionRepository projPropSectionRepo;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProcessTypeRepository processTypeRepo;
	@Autowired
	private NotificationService notificationService;


	public UUID commenceQprToDonor(AuthPrincipal initiatorPrincipal) {
		final UUID initiatorUserId = initiatorPrincipal.getUserId();
		//we will also receive body.optionalUserId (a JV guy!)


		QprToDonor qprToDonor = new QprToDonor();

		qprToDonor.setInitiatedBy(userRepo.getOne(initiatorUserId));
		qprToDonor.setStatus(ProcessStatus.DRAFT.getPersistenceValue());

		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.QPR_TO_DONOR.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for this process");
		}

		for (SectionTemplate st : sts) {
			QprToDonorSection ps = new QprToDonorSection();
			ps.setName(st.getSection().getName());
			ps.setPassingScore(st.getPassingScore());
			ps.setTotalScore(st.getTotalScore());
			ps.setTemplateType(st.getTemplateType());
			ps.setTemplate(st.getTemplate());
			ps.setSme(st.getSection().getSme());
			ps.setSectionRef(st.getSection());

			qprToDonor.addSection(ps);
		}


		qprToDonor = qprToDonorRepo.save(qprToDonor);
		
		try {
			notificationService.sendPlainTextEmail(initiatorPrincipal.getEmail(),
					initiatorPrincipal.getFullName(), "Qpr To Donor Added Confirmation",
					"Your qpr to donor has been initiated. Please complete all the sections.");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return qprToDonor.getId();
	}

//	private User getProcessOwnerForThematicArea(UUID id) {
//		ThematicArea area = thematicAreaRepo.findById(id)
//				.orElseThrow(() -> new ValidationException("Invalid Thematic area ID"));
//
//		if (area.getProcessOwner() == null) {
//			throw new ValidationException("Process Owner is not defined for this thematic area");
//		}
//
//		return area.getProcessOwner();
//	}

	public QprToDonorItem getQprToDonorRequest(UUID id, AuthPrincipal principal) {
		final UUID userId = principal.getUserId();

		QprToDonor p = qprToDonorRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		QprToDonorItem dto = new QprToDonorItem();

		dto.setInitiatedBy(new UserLookupItem(p.getInitiatedBy().getId(), p.getInitiatedBy().getFullName()));
		dto.setStatus(p.getStatus());
		dto.setSubmittedAt(p.getCreatedDate());

		if (p.getInitiatedBy().getId().equals(userId)) {
			List<QprToDonorTask> reassignmentComments = qprtodnortaskRepo.findAllTasksForAssigneeAndRequest(userId, id);

			if (reassignmentComments != null && reassignmentComments.size() > 0) {
				QprToDonorTask lastTask = reassignmentComments.get(reassignmentComments.size() - 1);

				TaskItem ti = new TaskItem();
				ti.setComments(lastTask.getComments());
				ti.setEndDate(lastTask.getEndDate());
				ti.setStartDate(lastTask.getStartDate());
				ti.setStatus(lastTask.getStatus());

				dto.setReassignmentTask(ti);
			}
		}

		p.getSections().forEach(qs -> {
			SectionItem section = new SectionItem();

			section.setRevisionNo(qs.getRevisionNo());
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
			section.setReviewCompletedDate(qs.getReviewCompletedOn());
			section.setReassignmentStatus(qs.getReassignmentStatus());
			section.setOrderNum(qs.getSectionRef().getOrderNum());
			section.setReassignmentComments(qs.getReassignmentComments());

			List<QprToDonorTask> tasks = qprToDonorTaskRepo.findTasksForSection(qs.getId(), PageRequest.of(0, 1));

			if (tasks != null && tasks.size() > 0) {
				section.setReviewDeadline(tasks.get(0).getEndDate());
			}

			if (qs.getReviews() != null && qs.getReviews().size() > 0) {
				QprToDonorSectionReview latestReview = qs.getReviews().get(qs.getReviews().size() - 1);

				section.setReview(latestReview.getCreatedDate(), null, null, latestReview.getStatus(),
						latestReview.getComments());

				qs.getReviews().forEach(r -> {
					section.addReviewHistory(r.getCreatedDate(), null, null, r.getStatus(), r.getComments());

					GeneralCommentItem gci = new GeneralCommentItem();
					if (r.getReviewAddedBy() != null) {
						gci.setAddedBy(r.getReviewAddedBy().getFullName());
					}
					gci.setComment(r.getComments());
					gci.setCreatedAt(r.getCreatedDate());
					gci.setSections(Arrays.asList(r.getSectionRef().getName()));
					
					gci.setSectionsWithIds(Stream.of(new KeyValue(r.getSectionRef().getId(), r.getSectionRef().getName())).collect(Collectors.toSet()));
					
					dto.addComment(gci);
				});
			}

			dto.addSection(section);
		});

//		if (p.getPreAppraisal() != null) {
//			PreliminaryAppraisalItem preAppItem = new PreliminaryAppraisalItem();
//
//			preAppItem.setData(p.getPreAppraisal().getData());
//			preAppItem.setId(p.getPreAppraisal().getId());
//			preAppItem.setQName(p.getName());
//			preAppItem.setTemplate(p.getPreAppraisal().getTemplate());
//			preAppItem.setStartDate(p.getPreAppraisal().getStartDate());
//			preAppItem.setEndDate(p.getPreAppraisal().getEndDate());
//			preAppItem.setAssigned(p.getPreAppraisal().getAssignee().getId().equals(userId));
//			preAppItem.setCompletedDate(p.getPreAppraisal().getCompletedOn());
//
//			preAppItem.setStatus(p.getPreAppraisal().getStatus());
//			preAppItem.setIsMarkedTo(p.getPreAppraisal().getIsMarkedTo());
//			preAppItem.setSubStatus(p.getPreAppraisal().getSubStatus());
//
//			preAppItem.setCommentsByPo(p.getPreAppraisal().getCommentsByPo());
//
//			dto.setPreAppraisal(preAppItem);
//			dto.setSubStatus(p.getPreAppraisal().getStatus());
//
//
//		}

//		if (p.getExtendedAppraisal() != null) {
//			ExtendedAppraisal e = p.getExtendedAppraisal();
//			ExtendedAppraisalItem eaItem = new ExtendedAppraisalItem();
//			eaItem.setId(e.getId());
//			eaItem.setAssigned(e.getAssignee().getId().equals(userId));
//			eaItem.setAssignee(new UserLookupItem(e.getAssignee().getId(), e.getAssignee().getFullName()));
//			eaItem.setComments(e.getComments());
//			eaItem.setEndDate(e.getEndDate());
//			eaItem.setStartDate(e.getStartDate());
//			eaItem.setCompletedDate(e.getCompletedOn());
//			eaItem.setDecisionByDm(e.getDecisionByDm());
//
//			eaItem.setStatus(e.getStatus());
//			eaItem.setIsMarkedTo(e.getIsMarkedTo());
//			eaItem.setSubStatus(e.getSubStatus());
//
//			e.getSections().forEach(s -> {
//				ExtendedAppraisalSectionItem item = new ExtendedAppraisalSectionItem();
//				item.setAssigned(s.getSme().getId().equals(userId));
//				item.setData(s.getData());
//				item.setId(s.getId());
//				item.setSme(new UserLookupItem(s.getSme().getId(), s.getSme().getFullName()));
//				item.setStatus(s.getStatus());
//				item.setTemplate(s.getTemplate());
//				item.setTemplateType(s.getTemplateType());
//				item.setOrderNum(s.getSectionRef().getOrderNum());
//				eaItem.addSection(item);
//			});
//
//			dto.setExtendedAppraisal(eaItem);
//			dto.setSubStatus(e.getStatus());
//		}

//		String generalCommentsJSON = p.getGeneralComments();
//		List<QprToDonorGeneralCommentModel> generalComments = new ArrayList<QprToDonorGeneralCommentModel>();
//
//		if (generalCommentsJSON != null) {
//			try {
//				generalComments = objectMapper.readValue(generalCommentsJSON, objectMapper.getTypeFactory()
//						.constructCollectionType(List.class, QprToDonorGeneralCommentModel.class));
//			} catch (Exception e) {
//				throw new RuntimeException("General Comments are not null but couldn't read it as JSON", e);
//			}
//
//			generalComments.forEach(gc -> {
//				GeneralCommentItem gci = new GeneralCommentItem();
//				gci.setAddedBy(gc.getAddedBy().getName());
//				gci.setComment(gc.getComment());
//				gci.setCreatedAt(gc.getCreatedAt());
//				gci.setSections(gc.getSections().stream().map(gcs -> gcs.getName()).collect(Collectors.toList()));
//
//				dto.addComment(gci);
//			});
//		}
		return dto;
	}

	public List<QprToDonorListItem> getQprToDonorRequests(AuthPrincipal currentUser, ProcessStatus status) throws IOException {
		List<QprToDonor> props = new ArrayList<QprToDonor>();

				props.addAll(qprToDonorRepo.findRequestsForInitiatorOrSME(currentUser.getUserId()));
		List<QprToDonorListItem> dtos = new ArrayList<>();
		props.forEach(q -> {
			QprToDonorListItem ppli = new QprToDonorListItem(
					q.getId(),
					q.getInitiatedBy().getFullName(),
					q.getCreatedDate(),
					q.getStatus());
			dtos.add(ppli);
		});
		return dtos;
	}
	

	public void submitQprToDonorSection(UUID userId, UUID qprtodonorId, QprToDonorSectionRequest body,
			FormAction action) {
		QprToDonor p = qprToDonorRepo.findById(qprtodonorId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		QprToDonorSection section = p.getSections().stream().filter(s -> s.getId().equals(body.getId())).findAny()
				.orElseThrow(() -> new ValidationException("Invalid section ID"));

		section.setData(body.getData());

//		if (p.getStatus() != null && p.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())){
//			if (action.equals(FormAction.SAVE)) {
//				p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
//			} else if (action.equals(FormAction.SUBMIT)) {
//				p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
//			}
//		}

		if (section.getReassignmentStatus() != null
				&& section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
			section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
		}

		if (section.getReassignmentStatus() != null){
			if (p.getSections().stream()
					.allMatch(r -> r.getData() != null &&
							((r.getReassignmentStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()) ||
											r.getReassignmentStatus() == null)))) {
				p.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
			}
		}

		if (section.getReassignmentStatus() == null){
			if (p.getSections().stream()
					.allMatch(r -> r.getData() != null &&
							(r.getReassignmentStatus() == null))) {
				p.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
			}
		}

		qprToDonorRepo.save(p);

		if (action == FormAction.SUBMIT) {
			// TODO raise event
			// eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));
		}
	}

//	@Transactional
//	public void commencePreliminaryAppraisal(UUID processOwnerId, UUID proposalId,
//			CommencePreliminaryAppraisalRequest body) {
//		List<SectionTemplate> sts = sectionTemplateRepo
//				.findTemplatesForProcessType(ProcessType.PRELIMINARY_APPRAISAL.toString());
//
//		if (sts == null || sts.size() == 0) {
//			throw new ValidationException("No template defined for PRELIMINARY_APPRAISAL process");
//		}
//
//		User dmPAM = userService.getDMPAM()
//				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));
//
//		QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		PreliminaryAppraisal appraisal = new PreliminaryAppraisal();
//		appraisal.setAssignee(dmPAM);
//		appraisal.setPassingScore(sts.get(0).getPassingScore());
//		appraisal.setTotalScore(sts.get(0).getTotalScore());
//		appraisal.setTemplateType(sts.get(0).getTemplateType());
//		appraisal.setTemplate(sts.get(0).getTemplate());
//		appraisal.setStartDate(body.getStartDate());
//		appraisal.setEndDate(body.getEndDate());
//		appraisal.setCommentsByPo(body.getComments());
//		appraisal.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//
//		proposal.setPreAppraisal(appraisal);
//		proposal.setStatus(ProcessStatus.PRELIMINARY_APPRAISAL.getPersistenceValue());
//	}

//	@Transactional
//	public void submitPreliminaryAppraisal(UUID userId, UUID proposalId, PreliminaryAppraisalRequest body) {
//		QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		PreliminaryAppraisal appraisal = proposal.getPreAppraisal();
//
//		User dmPAM = userService.getDMPAM()
//				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));
//
//		if (!dmPAM.getId().equals(userId)) {
//			throw new ValidationException(
//					"Only DM PAM can add Pre-Appriasal. Authorized User is: " + dmPAM.getFullName());
//		}
//
//		appraisal.setData(body.getData());
//		appraisal.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
//		appraisal.setCompletedOn(new Date());
//
//		proposal.setPreAppraisal(appraisal);
//	}

//	public List<PreliminaryAppraisalListItem> getAllPreliminaryAppraisals(UUID userId, ProcessStatus status) {
//		List<PreliminaryAppraisal> preApps;
//
//		if (status != null) {
//			preApps = preAppRepo.findAllAppraisalsForAssigneeAndStatus(userId, status.getPersistenceValue());
//		} else {
//			preApps = preAppRepo.findAllAppraisalsForAssignee(userId);
//		}
//
//		List<PreliminaryAppraisalListItem> dtos = new ArrayList<>();
//
//		preApps.forEach(p -> {
//			PreliminaryAppraisalListItem dto = new PreliminaryAppraisalListItem();
//
//			dto.setFipName(p.getProposalRef().getInitiatedBy().getFullName());
//			dto.setId(p.getId());
//			dto.setQId(p.getProposalRef().getId());
//			dto.setQName(p.getProposalRef().getName());
//
//			dtos.add(dto);
//		});
//
//		return dtos;
//	}

//	public PreliminaryAppraisalItem getPreliminaryAppraisal(UUID id) {
//		PreliminaryAppraisal preApp = preAppRepo.findById(id)
//				.orElseThrow(() -> new ValidationException("Invalid Appraisal ID"));
//
//		PreliminaryAppraisalItem dto = new PreliminaryAppraisalItem();
//
//		dto.setData(preApp.getData());
//		dto.setId(preApp.getId());
//		dto.setQName(preApp.getName());
//		dto.setTemplate(preApp.getTemplate());
//
//		return dto;
//	}

//	@Transactional
//	public ExtendedAppraisalItem commenceExtendedAppraisal(UUID processOwnerId, UUID proposalId,
//			CommenceExtendedAppraisalRequest body) {
//		List<SectionTemplate> sts = sectionTemplateRepo
//				.findTemplatesForProcessType(ProcessType.EXTENDED_APPRAISAL.toString());
//
//		if (sts == null || sts.size() == 0) {
//			throw new ValidationException("No template defined for EXTENDED_APPRAISAL process");
//		}
//
//		User dmPAM = userService.getDMPAM()
//				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));
//
//		QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		ExtendedAppraisal e = new ExtendedAppraisal();
//		e.setAssignee(dmPAM);
//		e.setComments(body.getComments());
//		e.setStartDate(body.getStartDate());
//		e.setEndDate(body.getEndDate());
//		e.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//
//
//		for (SectionTemplate t : sts) {
//			ExtendedAppraisalSection s = new ExtendedAppraisalSection();
//			s.setSme(t.getSection().getSme());
//			s.setTemplate(t.getTemplate());
//			s.setTemplateType(t.getTemplateType());
//			s.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//			s.setSectionRef(t.getSection());
//
//			e.addSection(s);
//		}
//
//		proposal.setExtendedAppraisal(e);
//
//		proposal.setStatus(ProcessStatus.EXTENDED_APPRAISAL.getPersistenceValue());
//
//		try {
//			proposal = qprToDonorRepo.save(proposal);
//			e = proposal.getExtendedAppraisal();
//		} catch (Exception ex) {
//			throw new RuntimeException("An error occurred while commencing Extended Appraisal", ex);
//		}
//
//		ExtendedAppraisalItem dto = new ExtendedAppraisalItem();
//		dto.setId(e.getId());
//		dto.setAssigned(e.getAssignee().getId().equals(processOwnerId));
//		dto.setAssignee(new UserLookupItem(e.getAssignee().getId(), e.getAssignee().getFullName()));
//		dto.setComments(e.getComments());
//		dto.setEndDate(e.getEndDate());
//		dto.setStartDate(e.getStartDate());
//		dto.setStatus(e.getStatus());
//
//		dto.setDecisionByDm(e.getDecisionByDm());
//
//		e.getSections().forEach(s -> {
//			ExtendedAppraisalSectionItem item = new ExtendedAppraisalSectionItem();
//			item.setAssigned(s.getSme().getId().equals(processOwnerId));
//			item.setData(s.getData());
//			item.setId(s.getId());
//			item.setSme(new UserLookupItem(s.getSme().getId(), s.getSme().getFullName()));
//			item.setStatus(s.getStatus());
//			item.setTemplate(s.getTemplate());
//			item.setTemplateType(s.getTemplateType());
//
//			dto.addSection(item);
//		});
//
//		return dto;
//	}

//	@Transactional
//	public void submitExtendedAppraisalSection(UUID userId, UUID extAppraisalId, ExtendedAppraisalSectionRequest body) {
//		ExtendedAppraisal a = extAppRepo.findById(extAppraisalId)
//				.orElseThrow(() -> new ValidationException("Invalid Extended Appraisal ID"));
//
//		ExtendedAppraisalSection section = a.getSections().stream().filter(s -> s.getId().equals(body.getId()))
//				.findAny().orElseThrow(() -> new ValidationException("Invalid ID"));
//
//		if (!section.getSme().getId().equals(userId)) {
//			throw new ValidationException("Unauthorized. Authorized User is: " + section.getSme().getFullName());
//		}
//
//		section.setData(body.getData());
//		section.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
//
//		boolean allSectionsCompleted = a.getSections().stream()
//				.allMatch(s -> s.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()));
//
//		if (allSectionsCompleted) {
//			a.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
//			a.setCompletedOn(new Date());
//			// TODO Also update Proposal Status
//		}
//	}

//	@Transactional
//	public void assignExtendedAppraisalSection(UUID userId, UUID extAppraisalId, AssignExtendedAppraisalSectionRequest body) {
//		ExtendedAppraisal a = extAppRepo.findById(extAppraisalId)
//				.orElseThrow(() -> new ValidationException("Invalid Extended Appraisal ID"));
//
//		ExtendedAppraisalSection section = a.getSections().stream().filter(s -> s.getId().equals(body.getId()))
//				.findAny().orElseThrow(() -> new ValidationException("Invalid ID"));
//		section.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//	}
//
//	@Transactional
//	public void extendedAppraisalDecisionByDm(UUID userId, UUID extAppraisalId) {
//		ExtendedAppraisal a = extAppRepo.findById(extAppraisalId)
//				.orElseThrow(() -> new ValidationException("Invalid Extended Appraisal ID"));
//		a.setDecisionByDm(ProcessStatus.APPROVED.getPersistenceValue());
//	}

//	@Transactional
//	public void submitImplementationPlan(UUID userId, UUID proposalId, AddImplementationPlanRequest body) {
//		QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		ProjectImplementationPlan pip = new ProjectImplementationPlan();
//		pip.setImplementationPlan(body.getImplementationPlan());
//
//		proposal.setPip(pip);
//	}

//	@Transactional
//	public void submitGrantImplementationAgreement(UUID userId, UUID proposalId,
//			AddGrantImplementationAgreementRequest body) {
//		QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
//				.findById(ProcessStatus.GIA.getPersistenceValue())
//				.orElseThrow(() -> new ValidationException("GIA Process is not defined."));
//
//		if (giaProcessType.getOwner() == null) {
//			throw new ValidationException(
//					"Process Owner is not defined for this process. To continue, admin should define Process Owner for this process first.");
//		}
//
//		if (!userId.equals(giaProcessType.getOwner().getId())) {
//			throw new ValidationException(
//					"Unauthorized. Authorized User is: " + giaProcessType.getOwner().getFullName());
//		}
//
//		GrantImplementationAgreement gia = new GrantImplementationAgreement();
//		gia.setData(body.getData());
//		gia.setAssignee(giaProcessType.getOwner());
//
//		if (body.getReviewers() != null) {
//			body.getReviewers().forEach(rv -> {
//				GrantImplementationAgreementReview review = new GrantImplementationAgreementReview();
//				review.setAssignee(userRepo.getOne(rv));
//				review.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//				review.setPoComments(body.getPoComments());
//				review.setStartDate(body.getStartDate());
//				review.setEndDate(body.getEndDate());
//				review.setQRef(proposal);
//				gia.addReview(review);
//			});
//
//			gia.setSubStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
//		}
//		gia.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//		proposal.setGia(gia);
//		proposal.setStatus(ProcessStatus.GIA.getPersistenceValue());
//	}

//	@Transactional
//	public void addGrantImplementationAgreementReview(UUID userId, UUID proposalId,
//			AddGrantImplementationAgreementReviewRequest body) {
//		final QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		if (proposal.getGia() == null) {
//			throw new ValidationException("Cannot add review. GIA does not exist for this proposal");
//		}
//
//		if (proposal.getGia().getReviews() == null || proposal.getGia().getReviews().size() == 0) {
//			throw new ValidationException("You're unauthorized to add review to this request.");
//		}
//
//		final GrantImplementationAgreementReview review = proposal.getGia().getReviews().stream()
//				.filter(r -> r.getAssignee().getId().equals(userId)).findAny()
//				.orElseThrow(() -> new ValidationException("You're unauthorized to add review to this request."));
//
//		review.setComments(body.getComments());
//		review.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
//
//		if (proposal.getGia().getReviews().stream()
//				.allMatch(r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()))) {
//			proposal.getGia().setSubStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
//		}
//	}

//	@Transactional
//	public void updateGrantImplementationAgreementStatus(UUID proposalId, ProcessStatus status,
//			Date checklistDeadline) {
//		final QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		if (proposal.getGia() == null) {
//			throw new ValidationException("GIA does not exist for this proposal");
//		}
//
//		if (proposal.getGia().getReviews() != null
//				&& proposal.getGia().getSubStatus() != null &&
//				proposal.getGia().getSubStatus().equals(ProcessStatus.REVIEW_PENDING.getPersistenceValue())) {
//			throw new ValidationException("Cannot change Status. There are still Pending Reviews for GIA");
//		}
//
//		proposal.getGia().setStatus(status.getPersistenceValue());
//
//		if (status.equals(ProcessStatus.APPROVED)) {
//			GIAChecklist checklist = new GIAChecklist();
//			checklist.setDeadline(checklistDeadline);
//			checklist.setStatus(ProcessStatus.PENDING.getPersistenceValue());
//
//			proposal.setGiaChecklist(checklist);
//			proposal.setStatus(ProcessStatus.GIA_CHECKLIST.getPersistenceValue());
//		}
//	}

//	@Transactional
//	public void submitGIAChecklist(UUID proposalId, AddGIAChecklistRequest body) {
//		final QprToDonor proposal = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		if (proposal.getGiaChecklist() == null) {
//			throw new ValidationException("GIA checklist does not exist for this proposal");
//		}
//
//		proposal.getGiaChecklist().setData(body.getData());
//		proposal.getGiaChecklist().setTemplate(body.getTemplate());
//		proposal.getGiaChecklist().setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
//	}

	@Transactional
	public void addQprToDonorTask(UUID sectionId, UUID currentUserId, AddProposalTaskRequest body) {
		QprToDonorSection section = projPropSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));

		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("Missing Configuration - DM PAM not defined"));

		if (!currentUserId.equals(dmPAM.getId())) {
			throw new ValidationException("Only DM PAM can add tasks. Authorized user is: " + dmPAM.getFullName());
		}

		QprToDonorTask task = new QprToDonorTask();

		task.setStartDate(body.getStartDate());
		task.setEndDate(body.getEndDate());
		task.setComments(body.getComments());
		task.setSection(section);
		task.setQprtodonor(section.getQprtodonorRef());
		task.setAssignee(section.getSme());
		task.setStatus(TaskStatus.PENDING.getPersistenceValue());

		section.setReviewStatus(ReviewStatus.PENDING.getPersistenceValue());

		qprToDonorTaskRepo.save(task);

		// TODO - trigger notification event
	}

//	@Transactional
//	public void updateProposalStatus(UUID proposalId, UUID userId, ProcessStatus status, ProcessStatus subStatus, OfferLetterUpdateRequest body) {
//
//		User u = userRepo.findById(userId)
//				.orElseThrow(() -> new ValidationException("Invalid request ID"));
//
//		Organisation userOrg = u.getOrg();
//
//		int user_org = 0;	//1 for NDRMF, 2 for FIP, 3 for Govt_fip
//
//		QprToDonor p = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid request ID"));
//
//		if (p.getStatus().equals(ProcessStatus.PRELIMINARY_APPRAISAL.getPersistenceValue())) {
//			PreliminaryAppraisal app = p.getPreAppraisal();
//
//			if (status.equals(ProcessStatus.MARKED_TO_GM) || status.equals(ProcessStatus.MARKED_TO_CEO)) {
//				app.setIsMarkedTo(status.getPersistenceValue());
//				app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
//			}
//			else if (status.equals(ProcessStatus.APPROVED) || status.equals(ProcessStatus.REJECTED)) {
//				app.setSubStatus(status.getPersistenceValue());
//			}
//			else {
//				p.setStatus(status.getPersistenceValue());
//			}
//		}
//		else if (p.getStatus().equals(ProcessStatus.EXTENDED_APPRAISAL.getPersistenceValue())) {
//			ExtendedAppraisal app = p.getExtendedAppraisal();
//
//			if (status.equals(ProcessStatus.MARKED_TO_GM) || status.equals(ProcessStatus.MARKED_TO_CEO)) {
//				app.setIsMarkedTo(status.getPersistenceValue());
//				app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
//			} else if (status.equals(ProcessStatus.APPROVED) || status.equals(ProcessStatus.REJECTED)) {
//				app.setSubStatus(status.getPersistenceValue());
//			}
//			else {
//				p.setStatus(status.getPersistenceValue());
//			}
//		}
//		else if(
//			p.getStatus().equals(ProcessStatus.OFFER_LETTER.getPersistenceValue())
//			&& status == ProcessStatus.OFFER_LETTER
//		){
//			if (!body.equals(null)) {
//				p.getOfferLetter().setGmMarkingStatus(body.getGmStatus());
//				p.getOfferLetter().setFipMarkingStatus(body.getFipStatus());
//				p.getOfferLetter().setFIP_comments(body.getFipComments());
//				p.getOfferLetter().setGM_comments(body.getGmComments());
//				p.getOfferLetter().setExpiryDate(body.getExpiryDate());
//				p.getOfferLetter().setFIP_response(body.getFipResponse());
//				p.getOfferLetter().setGM_response(body.getGmResponse());
//			}
//		}
//		else if(status == ProcessStatus.OFFER_LETTER && !p.getStatus().equals(ProcessStatus.OFFER_LETTER.getPersistenceValue())) {
//			OfferLetter oL = new OfferLetter();
//			oL.setStatus(subStatus.getPersistenceValue());
//			oL.setQRef(p);
//			p.setOfferLetter(oL);
//			p.setStatus(ProcessStatus.OFFER_LETTER.getPersistenceValue());
//			oL = offerLetterRepo.save(oL);
//		}
//		else {
//			p.setStatus(status.getPersistenceValue());
//		}
//	}

//	@Transactional
//	public String addProposalAttachment(
//			UUID proposalId,
//			UUID userId,
//			ProcessStatus stage,
//			MultipartFile file) throws IOException {
//		QprToDonor p = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		rFile persistedFile = fileStoreService.saveFile(file, userId);
//
//		QprToDonorAttachment attachment = new QprToDonorAttachment();
//		attachment.setFileRef(persistedFile);
//		attachment.setPicByte(compressBytes(file.getBytes()));
//		attachment.setStage(stage.getPersistenceValue());
//
//
//		p.addAttachement(attachment);
//		projectProposalAttachmentRepo.save(attachment);
//
//		if (stage == ProcessStatus.OFFER_LETTER) {
//			OfferLetter oL = p.getOfferLetter();
//			oL.setFileRef(persistedFile);
////			offerLetterRepo.save(oL);
//			p.setOfferLetter(oL);
//		}
//
//		if (stage == ProcessStatus.UPLOAD_PC1) {
//			p.setStatus(ProcessStatus.UPLOAD_PDRMC.getPersistenceValue());
//		} else if (stage == ProcessStatus.UPLOAD_PDRMC) {
//			p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
//		}
//
////		qprToDonorRepo.save(p);
//
//		return persistedFile.getPath();
//	}
	
//	@Transactional
//	public String updateProposalOfferLetterStatus(UUID proposalId, UUID userId, ProcessStatus status) throws IOException {
//		User u = userRepo.findById(userId)
//				.orElseThrow(() -> new ValidationException("Invalid User ID"));
//		String response = "Successful";
//		QprToDonor p = qprToDonorRepo.findById(proposalId)
//				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
//
//		List<QprToDonorAttachment> attachments =
//				projectProposalAttachmentRepo.findQprToDonorAttachmentByProposalIdAndStage(proposalId,
//						ProcessStatus.OFFER_LETTER.getPersistenceValue());
//		if (attachments.size() == 0)
//		{
//			return "No offer letter file attached.";
//		}
//		QprToDonorAttachment attachment = attachments.get(0);
//		if (p.getStatus() == ProcessStatus.OFFER_LETTER.getPersistenceValue() &&
//				(attachment.getGmMarkingStatus() == ProcessStatus.PENDING.getPersistenceValue() ||
//				attachment.getGmMarkingStatus() == ProcessStatus.UNDER_REVIEW.getPersistenceValue())) {
//			attachment.setGmMarkingStatus(ProcessStatus.APPROVED.getPersistenceValue());
//			attachment.setFipMarkingStatus(ProcessStatus.PENDING.getPersistenceValue());
//		}
//		else if(p.getStatus() == ProcessStatus.OFFER_LETTER.getPersistenceValue() &&
//				attachment.getGmMarkingStatus() == ProcessStatus.PENDING.getPersistenceValue()) {
//
//		}
//
//		return response;
//	}
	
//	@Transactional
//	public List<Object> readProposalAttachmentByStage(UUID proposalId, ProcessStatus stage) {
//
//		List<Object> fileData = projectProposalAttachmentRepo.findAttachedFileIdsByProposalIdAndStage(proposalId, stage.getPersistenceValue());
//		return fileData;
//	}

//	@Transactional
//	public List<QprToDonorAttachment> readProposalAttachmentsByProposalId(UUID proposalId) {
//		List<QprToDonorAttachment> fileData = projectProposalAttachmentRepo.findAttachedFileIdsByProposalId(proposalId);
//		return fileData;
//	}
//
//	@Transactional
//	public List getAttachmentsByFileNameAndPath(String fileName, String filePath) {
//		List<QprToDonorAttachment> fileData;
//		fileData = projectProposalAttachmentRepo.findAttachedFileByfileNameAndPath(fileName, filePath);
//		return fileData;
//
//	}

	@Transactional
	public void reassignQprToDonorSections(UUID qprtodonorId, UUID userId, Set<UUID> sectionIds, String comments) {
		QprToDonor p = qprToDonorRepo.findById(qprtodonorId)
				.orElseThrow(() -> new ValidationException("Invalid QPR TO DONOR ID"));

		sectionIds.forEach(sId -> {
			QprToDonorSection section = p.getSections()
					.stream()
					.filter(s -> s.getId().equals(sId))
					.findFirst()
					.orElseThrow(() -> new ValidationException("Invalid QPR TO DONOR Section ID"));

			section.setReassignmentStatus(ProcessStatus.PENDING.getPersistenceValue());
			section.setReassignmentComments(comments);
		});

		p.setStatus(ProcessStatus.PENDING.getPersistenceValue());
	}

//	public OfferLetterItem getOfferLetter(AuthPrincipal principal, UUID proposalId) {
//		OfferLetter oL = offerLetterRepo.findAllRequestsByProposalId(proposalId);
//
//		OfferLetterItem dto = new OfferLetterItem();
//		if(oL != null) {
//			dto.setId(oL.getId());
//			dto.setData(oL.getData());
//			dto.setExpiryDate(oL.getExpiryDate());
//			dto.setFIP_comments(oL.getFIP_comments());
//			dto.setGM_comments(oL.getGM_comments());
//			dto.setFipMarkingStatus(oL.getFipMarkingStatus());
//			dto.setGmMarkingStatus(oL.getGmMarkingStatus());
//			dto.setGmResponse(oL.getGM_response());
//			dto.setFipResponse(oL.getFIP_response());
//			dto.setStage(oL.getStage());
//			dto.setStatus(oL.getStatus());
//			dto.setQRefId(oL.getProposalRef().getId());
////			dto.setFileRef(oL.getFileRef());
//			return dto;
//		}
//		return null;
//	}

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}

		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();

	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}