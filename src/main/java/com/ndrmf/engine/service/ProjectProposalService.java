package com.ndrmf.engine.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.common.File;
import com.ndrmf.engine.dto.AddGIAChecklistRequest;
import com.ndrmf.engine.dto.AddGrantImplementationAgreementRequest;
import com.ndrmf.engine.dto.AddGrantImplementationAgreementReviewRequest;
import com.ndrmf.engine.dto.AddImplementationPlanRequest;
import com.ndrmf.engine.dto.AddProposalTaskRequest;
import com.ndrmf.engine.dto.CommenceExtendedAppraisalRequest;
import com.ndrmf.engine.dto.CommencePreliminaryAppraisalRequest;
import com.ndrmf.engine.dto.CommenceProjectProposalRequest;
import com.ndrmf.engine.dto.ExtendedAppraisalItem;
import com.ndrmf.engine.dto.ExtendedAppraisalItem.ExtendedAppraisalSectionItem;
import com.ndrmf.engine.dto.ExtendedAppraisalSectionRequest;
import com.ndrmf.engine.dto.GIAChecklistItem;
import com.ndrmf.engine.dto.GeneralCommentItem;
import com.ndrmf.engine.dto.GrantImplmentationItem;
import com.ndrmf.engine.dto.GrantImplmentationItem.GiaReviewItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalListItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalRequest;
import com.ndrmf.engine.dto.ProjectProposalItem;
import com.ndrmf.engine.dto.ProjectProposalListItem;
import com.ndrmf.engine.dto.ProjectProposalSectionRequest;
import com.ndrmf.engine.dto.SectionItem;
import com.ndrmf.engine.model.ExtendedAppraisal;
import com.ndrmf.engine.model.ExtendedAppraisalSection;
import com.ndrmf.engine.model.GIAChecklist;
import com.ndrmf.engine.model.GrantImplementationAgreement;
import com.ndrmf.engine.model.GrantImplementationAgreementReview;
import com.ndrmf.engine.model.PreliminaryAppraisal;
import com.ndrmf.engine.model.ProjectImplementationPlan;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.ProjectProposalAttachment;
import com.ndrmf.engine.model.ProjectProposalGeneralCommentModel;
import com.ndrmf.engine.model.ProjectProposalSection;
import com.ndrmf.engine.model.ProjectProposalSectionReview;
import com.ndrmf.engine.model.ProjectProposalTask;
import com.ndrmf.engine.repository.ExtendedAppraisalRepository;
import com.ndrmf.engine.repository.PreliminaryAppraisalRepository;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.ProjectProposalSectionRepository;
import com.ndrmf.engine.repository.ProjectProposalTaskRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.setting.repository.ThematicAreaRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.KeyValue;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;
import com.ndrmf.util.enums.ReassignmentStatus;
import com.ndrmf.util.enums.ReviewStatus;
import com.ndrmf.util.enums.TaskStatus;

@Service
public class ProjectProposalService {
	@Autowired
	private ProjectProposalRepository projProposalRepo;
	@Autowired
	private SectionTemplateRepository sectionTemplateRepo;
	@Autowired
	private ThematicAreaRepository thematicAreaRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ProjectProposalTaskRepository ptaskRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private PreliminaryAppraisalRepository preAppRepo;
	@Autowired
	private ExtendedAppraisalRepository extAppRepo;
	@Autowired
	private ProjectProposalTaskRepository projPropTaskRepo;
	@Autowired
	private ProjectProposalSectionRepository projPropSectionRepo;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private FileStoreService fileStoreService;
	@Autowired
	private ProcessTypeRepository processTypeRepo;
	@Autowired
	private NotificationService notificationService;

	public UUID commenceProjectProposal(AuthPrincipal initiatorPrincipal, CommenceProjectProposalRequest body) {
		final UUID initiatorUserId = initiatorPrincipal.getUserId();

		if (body.getThematicAreaId() == null) {
			throw new ValidationException("Thematic area cannot be null");
		}
		ProjectProposal p = new ProjectProposal();

		p.setName(body.getName());
		p.setInitiatedBy(userRepo.getOne(initiatorUserId));
		p.setProcessOwner(this.getProcessOwnerForThematicArea(body.getThematicAreaId()));
		p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		p.setThematicArea(thematicAreaRepo.getOne(body.getThematicAreaId()));

		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.PROJECT_PROPOSAL.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for this process");
		}

		for (SectionTemplate st : sts) {
			ProjectProposalSection ps = new ProjectProposalSection();
			ps.setName(st.getSection().getName());
			ps.setPassingScore(st.getPassingScore());
			ps.setTotalScore(st.getTotalScore());
			ps.setTemplateType(st.getTemplateType());
			ps.setTemplate(st.getTemplate());
			ps.setSme(st.getSection().getSme());
			ps.setSectionRef(st.getSection());

			p.addSection(ps);
		}

		if (initiatorPrincipal.getRoles().contains(SystemRoles.ORG_GOVT)) {
			p.setStatus(ProcessStatus.UPLOAD_PC1.getPersistenceValue());
		}

		p = projProposalRepo.save(p);
		
		try {
			notificationService.sendPlainTextEmail(initiatorPrincipal.getEmail(),
					initiatorPrincipal.getFullName(), "Project Proposal Submission Confirmation",
					"Your prposal has been initiated. Please complete all the sections.");	
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return p.getId();
	}

	private User getProcessOwnerForThematicArea(UUID id) {
		ThematicArea area = thematicAreaRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid Thematic area ID"));

		if (area.getProcessOwner() == null) {
			throw new ValidationException("Process Owner is not defined for this thematic area");
		}

		return area.getProcessOwner();
	}

	public ProjectProposalItem getProjectProposalRequest(UUID id, AuthPrincipal principal) {
		final UUID userId = principal.getUserId();

		ProjectProposal p = projProposalRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		ProjectProposalItem dto = new ProjectProposalItem();

		dto.setInitiatedBy(new UserLookupItem(p.getInitiatedBy().getId(), p.getInitiatedBy().getFullName()));
		dto.setOwner(p.getProcessOwner().getId().equals(userId));
		dto.setProcessOwner(new UserLookupItem(p.getProcessOwner().getId(), p.getProcessOwner().getFullName()));
		dto.setStatus(p.getStatus());
		dto.setSubmittedAt(p.getCreatedDate());

		if (p.getInitiatedBy().getRoles().stream().anyMatch(r -> r.getOrg().getId() == SystemRoles.ORG_GOVT_ID)) {
			dto.setGovFip(true);
		}

		if (p.getInitiatedBy().getId().equals(userId)) {
			List<ProjectProposalTask> reassignmentComments = ptaskRepo.findAllTasksForAssigneeAndRequest(userId, id);

			if (reassignmentComments != null && reassignmentComments.size() > 0) {
				ProjectProposalTask lastTask = reassignmentComments.get(reassignmentComments.size() - 1);

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

			List<ProjectProposalTask> tasks = projPropTaskRepo.findTasksForSection(qs.getId(), PageRequest.of(0, 1));

			if (tasks != null && tasks.size() > 0) {
				section.setReviewDeadline(tasks.get(0).getEndDate());
			}

			if (qs.getReviews() != null && qs.getReviews().size() > 0) {
				ProjectProposalSectionReview latestReview = qs.getReviews().get(qs.getReviews().size() - 1);

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

		if (p.getPreAppraisal() != null) {
			PreliminaryAppraisalItem preAppItem = new PreliminaryAppraisalItem();

			preAppItem.setData(p.getPreAppraisal().getData());
			preAppItem.setId(p.getPreAppraisal().getId());
			preAppItem.setProposalName(p.getName());
			preAppItem.setTemplate(p.getPreAppraisal().getTemplate());
			preAppItem.setStartDate(p.getPreAppraisal().getStartDate());
			preAppItem.setEndDate(p.getPreAppraisal().getEndDate());
			preAppItem.setAssigned(p.getPreAppraisal().getAssignee().getId().equals(userId));
			preAppItem.setCompletedDate(p.getPreAppraisal().getCompletedOn());

			preAppItem.setStatus(p.getPreAppraisal().getStatus());
			preAppItem.setIsMarkedTo(p.getPreAppraisal().getIsMarkedTo());
			preAppItem.setSubStatus(p.getPreAppraisal().getSubStatus());

			dto.setPreAppraisal(preAppItem);
			dto.setSubStatus(p.getPreAppraisal().getStatus());
		}

		if (p.getExtendedAppraisal() != null) {
			ExtendedAppraisal e = p.getExtendedAppraisal();
			ExtendedAppraisalItem eaItem = new ExtendedAppraisalItem();
			eaItem.setId(e.getId());
			eaItem.setAssigned(e.getAssignee().getId().equals(userId));
			eaItem.setAssignee(new UserLookupItem(e.getAssignee().getId(), e.getAssignee().getFullName()));
			eaItem.setComments(e.getComments());
			eaItem.setEndDate(e.getEndDate());
			eaItem.setStartDate(e.getStartDate());
			eaItem.setCompletedDate(e.getCompletedOn());

			eaItem.setStatus(e.getStatus());
			eaItem.setIsMarkedTo(e.getIsMarkedTo());
			eaItem.setSubStatus(e.getSubStatus());

			e.getSections().forEach(s -> {
				ExtendedAppraisalSectionItem item = new ExtendedAppraisalSectionItem();
				item.setAssigned(s.getSme().getId().equals(userId));
				item.setData(s.getData());
				item.setId(s.getId());
				item.setSme(new UserLookupItem(s.getSme().getId(), s.getSme().getFullName()));
				item.setStatus(s.getStatus());
				item.setTemplate(s.getTemplate());
				item.setTemplateType(s.getTemplateType());

				eaItem.addSection(item);
			});

			dto.setExtendedAppraisal(eaItem);
			dto.setSubStatus(e.getStatus());
		}

		String generalCommentsJSON = p.getGeneralComments();
		List<ProjectProposalGeneralCommentModel> generalComments = new ArrayList<ProjectProposalGeneralCommentModel>();

		if (generalCommentsJSON != null) {
			try {
				generalComments = objectMapper.readValue(generalCommentsJSON, objectMapper.getTypeFactory()
						.constructCollectionType(List.class, ProjectProposalGeneralCommentModel.class));
			} catch (Exception e) {
				throw new RuntimeException("General Comments are not null but couldn't read it as JSON", e);
			}

			generalComments.forEach(gc -> {
				GeneralCommentItem gci = new GeneralCommentItem();
				gci.setAddedBy(gc.getAddedBy().getName());
				gci.setComment(gc.getComment());
				gci.setCreatedAt(gc.getCreatedAt());
				gci.setSections(gc.getSections().stream().map(gcs -> gcs.getName()).collect(Collectors.toList()));

				dto.addComment(gci);
			});
		}

		if (p.getPip() != null) {
			dto.setImplementationPlan(p.getPip().getImplementationPlan());
		}

		if (p.getGia() != null) {
			GrantImplmentationItem giaItem = new GrantImplmentationItem();
			giaItem.setData(p.getGia().getData());
			giaItem.setProcessOwner(
					new UserLookupItem(p.getGia().getAssignee().getId(), p.getGia().getAssignee().getFullName()));

			if (p.getGia().getReviews() != null) {
				p.getGia().getReviews().forEach(r -> {
					GiaReviewItem review = new GiaReviewItem();
					review.setAssigned(r.getAssignee().getId().equals(userId));
					review.setAssignee(new UserLookupItem(r.getAssignee().getId(), r.getAssignee().getFullName()));
					review.setComments(r.getComments());
					review.setId(r.getId());

					giaItem.addReview(review);
				});
			}

			giaItem.setStatus(p.getGia().getStatus());
			giaItem.setSubStatus(p.getGia().getSubStatus());

			dto.setGia(giaItem);
		} else {
			com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
					.findById(ProcessStatus.GIA.getPersistenceValue())
					.orElseThrow(() -> new ValidationException("GIA Process is not defined."));
			if (giaProcessType.getOwner() != null) {
				GrantImplmentationItem giaItem = new GrantImplmentationItem();
				giaItem.setProcessOwner(
						new UserLookupItem(giaProcessType.getOwner().getId(), giaProcessType.getOwner().getFullName()));

				giaItem.setStatus(ProcessStatus.NOT_INITIATED.getPersistenceValue());
				dto.setGia(giaItem);
			}
		}

		if (p.getGiaChecklist() != null) {
			GIAChecklistItem checklistItem = new GIAChecklistItem();
			checklistItem.setAssigned(userId.equals(p.getInitiatedBy().getId()));
			checklistItem.setAssignee(new UserLookupItem(p.getInitiatedBy().getId(), p.getInitiatedBy().getFullName()));
			checklistItem.setData(p.getGiaChecklist().getData());
			checklistItem.setTemplate(p.getGiaChecklist().getTemplate());
			checklistItem.setDeadline(p.getGiaChecklist().getDeadline());

			dto.setGiaChecklist(checklistItem);
		}

		return dto;
	}

	public List<ProjectProposalListItem> getProjectProposalRequests(AuthPrincipal currentUser, ProcessStatus status) {
		List<ProjectProposal> props;

		if (currentUser.getRoles() != null && (currentUser.getRoles().contains(SystemRoles.GM)
				|| currentUser.getRoles().contains(SystemRoles.CEO))) {
			if (status == null) {
				props = projProposalRepo.findAll();
			} else {
				props = projProposalRepo.findAllRequestsByStatus(status.getPersistenceValue());
			}
		} else {
			if (status == null) {
				props = projProposalRepo.findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(currentUser.getUserId());
			} else {
				props = projProposalRepo.findRequestsForOwnerOrInitiatorOrDMPAMOrSMEByStatus(currentUser.getUserId(),
						status.getPersistenceValue());
			}
		}

		List<ProjectProposalListItem> dtos = props.stream()
				.map(q -> new ProjectProposalListItem(q.getId(), q.getName(), q.getThematicArea().getName(),
						q.getInitiatedBy().getFullName(), q.getCreatedDate(), q.getStatus()))
				.collect(Collectors.toList());

		return dtos;
	}

	public void submitProposalSection(UUID userId, UUID proposalId, ProjectProposalSectionRequest body,
			FormAction action) {
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		if (!p.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())
				&& !p.getStatus().equals(ProcessStatus.REASSIGNED.getPersistenceValue())) {
			throw new ValidationException("Request is already: " + p.getStatus());
		}

		ProjectProposalSection section = p.getSections().stream().filter(s -> s.getId().equals(body.getId())).findAny()
				.orElseThrow(() -> new ValidationException("Invalid ID"));

		section.setData(body.getData());

		if (action == FormAction.SAVE) {
			p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		} else if (action == FormAction.SUBMIT) {
			p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		}

		if (section.getReassignmentStatus() != null
				&& section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
			section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
		}

		projProposalRepo.save(p);

		if (action == FormAction.SUBMIT) {
			// TODO raise event
			// eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));
		}
	}

	@Transactional
	public void commencePreliminaryAppraisal(UUID processOwnerId, UUID proposalId,
			CommencePreliminaryAppraisalRequest body) {
		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.PRELIMINARY_APPRAISAL.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for PRELIMINARY_APPRAISAL process");
		}

		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));

		ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		PreliminaryAppraisal appraisal = new PreliminaryAppraisal();
		appraisal.setAssignee(dmPAM);
		appraisal.setPassingScore(sts.get(0).getPassingScore());
		appraisal.setTotalScore(sts.get(0).getTotalScore());
		appraisal.setTemplateType(sts.get(0).getTemplateType());
		appraisal.setTemplate(sts.get(0).getTemplate());
		appraisal.setStartDate(body.getStartDate());
		appraisal.setEndDate(body.getEndDate());
		appraisal.setStatus(ProcessStatus.PENDING.getPersistenceValue());

		proposal.setPreAppraisal(appraisal);
		proposal.setStatus(ProcessStatus.PRELIMINARY_APPRAISAL.getPersistenceValue());
	}

	@Transactional
	public void submitPreliminaryAppraisal(UUID userId, UUID proposalId, PreliminaryAppraisalRequest body) {
		ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		PreliminaryAppraisal appraisal = proposal.getPreAppraisal();

		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));

		if (!dmPAM.getId().equals(userId)) {
			throw new ValidationException(
					"Only DM PAM can add Pre-Appriasal. Authorized User is: " + dmPAM.getFullName());
		}

		appraisal.setData(body.getData());
		appraisal.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		appraisal.setCompletedOn(new Date());

		proposal.setPreAppraisal(appraisal);
	}

	public List<PreliminaryAppraisalListItem> getAllPreliminaryAppraisals(UUID userId, ProcessStatus status) {
		List<PreliminaryAppraisal> preApps;

		if (status != null) {
			preApps = preAppRepo.findAllAppraisalsForAssigneeAndStatus(userId, status.getPersistenceValue());
		} else {
			preApps = preAppRepo.findAllAppraisalsForAssignee(userId);
		}

		List<PreliminaryAppraisalListItem> dtos = new ArrayList<>();

		preApps.forEach(p -> {
			PreliminaryAppraisalListItem dto = new PreliminaryAppraisalListItem();

			dto.setFipName(p.getProposalRef().getInitiatedBy().getFullName());
			dto.setId(p.getId());
			dto.setProposalId(p.getProposalRef().getId());
			dto.setProposalName(p.getProposalRef().getName());

			dtos.add(dto);
		});

		return dtos;
	}

	public PreliminaryAppraisalItem getPreliminaryAppraisal(UUID id) {
		PreliminaryAppraisal preApp = preAppRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid Appraisal ID"));

		PreliminaryAppraisalItem dto = new PreliminaryAppraisalItem();

		dto.setData(preApp.getData());
		dto.setId(preApp.getId());
		dto.setProposalName(preApp.getName());
		dto.setTemplate(preApp.getTemplate());

		return dto;
	}

	@Transactional
	public ExtendedAppraisalItem commenceExtendedAppraisal(UUID processOwnerId, UUID proposalId,
			CommenceExtendedAppraisalRequest body) {
		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.EXTENDED_APPRAISAL.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for EXTENDED_APPRAISAL process");
		}

		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));

		ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		ExtendedAppraisal e = new ExtendedAppraisal();
		e.setAssignee(dmPAM);
		e.setComments(body.getComments());
		e.setStartDate(body.getStartDate());
		e.setEndDate(body.getEndDate());
		e.setStatus(ProcessStatus.PENDING.getPersistenceValue());

		for (SectionTemplate t : sts) {
			ExtendedAppraisalSection s = new ExtendedAppraisalSection();
			s.setSme(t.getSection().getSme());
			s.setTemplate(t.getTemplate());
			s.setTemplateType(t.getTemplateType());
			s.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			s.setSectionRef(t.getSection());

			e.addSection(s);
		}

		proposal.setExtendedAppraisal(e);

		proposal.setStatus(ProcessStatus.EXTENDED_APPRAISAL.getPersistenceValue());

		try {
			proposal = projProposalRepo.save(proposal);
			e = proposal.getExtendedAppraisal();
		} catch (Exception ex) {
			throw new RuntimeException("An error occurred while commencing Extended Appraisal", ex);
		}

		ExtendedAppraisalItem dto = new ExtendedAppraisalItem();
		dto.setId(e.getId());
		dto.setAssigned(e.getAssignee().getId().equals(processOwnerId));
		dto.setAssignee(new UserLookupItem(e.getAssignee().getId(), e.getAssignee().getFullName()));
		dto.setComments(e.getComments());
		dto.setEndDate(e.getEndDate());
		dto.setStartDate(e.getStartDate());
		dto.setStatus(e.getStatus());

		e.getSections().forEach(s -> {
			ExtendedAppraisalSectionItem item = new ExtendedAppraisalSectionItem();
			item.setAssigned(s.getSme().getId().equals(processOwnerId));
			item.setData(s.getData());
			item.setId(s.getId());
			item.setSme(new UserLookupItem(s.getSme().getId(), s.getSme().getFullName()));
			item.setStatus(s.getStatus());
			item.setTemplate(s.getTemplate());
			item.setTemplateType(s.getTemplateType());

			dto.addSection(item);
		});

		return dto;
	}

	@Transactional
	public void submitExtendedAppraisalSection(UUID userId, UUID extAppraisalId, ExtendedAppraisalSectionRequest body) {
		ExtendedAppraisal a = extAppRepo.findById(extAppraisalId)
				.orElseThrow(() -> new ValidationException("Invalid Extended Appraisal ID"));

		ExtendedAppraisalSection section = a.getSections().stream().filter(s -> s.getId().equals(body.getId()))
				.findAny().orElseThrow(() -> new ValidationException("Invalid ID"));

		if (!section.getSme().getId().equals(userId)) {
			throw new ValidationException("Unauthorized. Authorized User is: " + section.getSme().getFullName());
		}

		section.setData(body.getData());
		section.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());

		boolean allSectionsCompleted = a.getSections().stream()
				.allMatch(s -> s.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()));

		if (allSectionsCompleted) {
			a.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
			a.setCompletedOn(new Date());
			// TODO Also update Proposal Status
		}
	}

	@Transactional
	public void submitImplementationPlan(UUID userId, UUID proposalId, AddImplementationPlanRequest body) {
		ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		ProjectImplementationPlan pip = new ProjectImplementationPlan();
		pip.setImplementationPlan(body.getImplementationPlan());

		proposal.setPip(pip);
	}

	@Transactional
	public void submitGrantImplementationAgreement(UUID userId, UUID proposalId,
			AddGrantImplementationAgreementRequest body) {
		ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
				.findById(ProcessStatus.GIA.getPersistenceValue())
				.orElseThrow(() -> new ValidationException("GIA Process is not defined."));

		if (giaProcessType.getOwner() == null) {
			throw new ValidationException(
					"Process Owner is not defined for this process. To continue, admin should define Process Owner for this process first.");
		}

		if (!userId.equals(giaProcessType.getOwner().getId())) {
			throw new ValidationException(
					"Unauthorized. Authorized User is: " + giaProcessType.getOwner().getFullName());
		}

		GrantImplementationAgreement gia = new GrantImplementationAgreement();
		gia.setData(body.getData());
		gia.setAssignee(giaProcessType.getOwner());

		if (body.getReviewers() != null) {
			body.getReviewers().forEach(rv -> {
				GrantImplementationAgreementReview review = new GrantImplementationAgreementReview();
				review.setAssignee(userRepo.getOne(rv));
				review.setStatus(ProcessStatus.PENDING.getPersistenceValue());

				gia.addReview(review);
			});

			gia.setSubStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
		}
		gia.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		proposal.setGia(gia);
		proposal.setStatus(ProcessStatus.GIA.getPersistenceValue());
	}

	@Transactional
	public void addGrantImplementationAgreementReview(UUID userId, UUID proposalId,
			AddGrantImplementationAgreementReviewRequest body) {
		final ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		if (proposal.getGia() == null) {
			throw new ValidationException("Cannot add review. GIA does not exist for this proposal");
		}

		if (proposal.getGia().getReviews() == null || proposal.getGia().getReviews().size() == 0) {
			throw new ValidationException("You're unauthorized to add review to this request.");
		}

		final GrantImplementationAgreementReview review = proposal.getGia().getReviews().stream()
				.filter(r -> r.getAssignee().getId().equals(userId)).findAny()
				.orElseThrow(() -> new ValidationException("You're unauthorized to add review to this request."));

		review.setComments(body.getComments());
		review.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());

		if (proposal.getGia().getReviews().stream()
				.allMatch(r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()))) {
			proposal.getGia().setSubStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		}
	}

	@Transactional
	public void updateGrantImplementationAgreementStatus(UUID proposalId, ProcessStatus status,
			Date checklistDeadline) {
		final ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		if (proposal.getGia() == null) {
			throw new ValidationException("GIA does not exist for this proposal");
		}

		if (proposal.getGia().getReviews() != null
				&& proposal.getGia().getSubStatus().equals(ProcessStatus.REVIEW_PENDING.getPersistenceValue())) {
			throw new ValidationException("Cannot change Status. There are still Pending Reviews for GIA");
		}

		proposal.getGia().setStatus(status.getPersistenceValue());

		if (status.equals(ProcessStatus.APPROVED)) {
			GIAChecklist checklist = new GIAChecklist();
			checklist.setDeadline(checklistDeadline);
			checklist.setStatus(ProcessStatus.PENDING.getPersistenceValue());

			proposal.setGiaChecklist(checklist);
			proposal.setStatus(ProcessStatus.GIA_CHECKLIST.getPersistenceValue());
		}
	}

	@Transactional
	public void submitGIAChecklist(UUID proposalId, AddGIAChecklistRequest body) {
		final ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		if (proposal.getGiaChecklist() == null) {
			throw new ValidationException("GIA checklist does not exist for this proposal");
		}

		proposal.getGiaChecklist().setData(body.getData());
		proposal.getGiaChecklist().setTemplate(body.getTemplate());
		proposal.getGiaChecklist().setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
	}

	@Transactional
	public void addProjectProposalTask(UUID sectionId, UUID currentUserId, AddProposalTaskRequest body) {
		ProjectProposalSection section = projPropSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));

		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("Missing Configuration - DM PAM not defined"));

		if (!currentUserId.equals(dmPAM.getId())) {
			throw new ValidationException("Only DM PAM can add tasks. Authorized user is: " + dmPAM.getFullName());
		}

		ProjectProposalTask task = new ProjectProposalTask();

		task.setStartDate(body.getStartDate());
		task.setEndDate(body.getEndDate());
		task.setComments(body.getComments());
		task.setSection(section);
		task.setProposal(section.getProposalRef());
		task.setAssignee(section.getSme());
		task.setStatus(TaskStatus.PENDING.getPersistenceValue());

		section.setReviewStatus(ReviewStatus.PENDING.getPersistenceValue());

		projPropTaskRepo.save(task);

		// TODO - trigger notification event
	}

	@Transactional
	public void updateProposalStatus(UUID proposalId, UUID userId, ProcessStatus status) {
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		if (p.getStatus().equals(ProcessStatus.PRELIMINARY_APPRAISAL.getPersistenceValue())) {
			PreliminaryAppraisal app = p.getPreAppraisal();

			if (status.equals(ProcessStatus.MARKED_TO_GM) || status.equals(ProcessStatus.MARKED_TO_CEO)) {
				app.setIsMarkedTo(status.getPersistenceValue());
				app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
			}
			else if (status.equals(ProcessStatus.APPROVED) || status.equals(ProcessStatus.REJECTED)) {
				app.setSubStatus(status.getPersistenceValue());
			}
			else {
				p.setStatus(status.getPersistenceValue());
			}
		}
		else if (p.getStatus().equals(ProcessStatus.EXTENDED_APPRAISAL.getPersistenceValue())) {
			ExtendedAppraisal app = p.getExtendedAppraisal();

			if (status.equals(ProcessStatus.MARKED_TO_GM) || status.equals(ProcessStatus.MARKED_TO_CEO)) {
				app.setIsMarkedTo(status.getPersistenceValue());
				app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
			} else if (status.equals(ProcessStatus.APPROVED) || status.equals(ProcessStatus.REJECTED)) {
				app.setSubStatus(status.getPersistenceValue());
			}
			else {
				p.setStatus(status.getPersistenceValue());
			}
		}
		else {
			p.setStatus(status.getPersistenceValue());
		}
	}

	@Transactional
	public UUID addProposalAttachment(UUID proposalId, UUID userId, ProcessStatus stage, MultipartFile file) {
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

		File persistedFile = fileStoreService.saveFile(file, userId);

		ProjectProposalAttachment attachment = new ProjectProposalAttachment();
		attachment.setFileRef(persistedFile);
		attachment.setStage(stage.getPersistenceValue());

		p.addAttachement(attachment);

		if (stage == ProcessStatus.UPLOAD_PC1) {
			p.setStatus(ProcessStatus.UPLOAD_PDRMC.getPersistenceValue());
		} else if (stage == ProcessStatus.UPLOAD_PDRMC) {
			p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		}

		return persistedFile.getId();
	}
	
	@Transactional
	public void reassignProposalToFIP(UUID proposalId, UUID userId, Set<UUID> sectionIds) {
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
		
		sectionIds.forEach(sId -> {
			ProjectProposalSection section = p.getSections()
					.stream()
					.filter(s -> s.getId().equals(sId))
					.findFirst()
					.orElseThrow(() -> new ValidationException("Invalid Section ID"));
			
			section.setReassignmentStatus(ProcessStatus.PENDING.getPersistenceValue());
		});
	}
}