package com.ndrmf.engine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;


import com.ndrmf.common.ApiResponse;
import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.common.rFile;
import com.ndrmf.engine.dto.ExtendedAppraisalItem.ExtendedAppraisalSectionItem;

import com.ndrmf.engine.dto.GrantImplmentationItem.GiaReviewItem;
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
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.setting.repository.ThematicAreaRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.Organisation;
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
import com.ndrmf.engine.model.OfferLetter;

import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;


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
    @Autowired
    private ProjectProposalAttachmentRepository projectProposalAttachmentRepo;
    @Autowired
    private OfferLetterRepository offerLetterRepo;
    @Autowired
    private GrantImplementationAgreementReviewRepository grantImplementationAgreementReviewRepository;


    public UUID commenceProjectProposal(AuthPrincipal initiatorPrincipal, CommenceProjectProposalRequest body) {
        final UUID initiatorUserId = initiatorPrincipal.getUserId();
        //we will also receive body.optionalUserId (a JV guy!)

        if (body.getThematicAreaId() == null) {
            throw new ValidationException("Thematic area cannot be null");
        }
        ProjectProposal p = new ProjectProposal();

        p.setName(body.getName());
        p.setType(body.getType());
        p.setInitiatedBy(userRepo.getOne(initiatorUserId));

        if (body.getJvUserID() != null)
            p.setJvUser(userRepo.getOne(body.getJvUserID()));

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

        p.setCreated_at(new Date());

        p = projProposalRepo.save(p);

        try {
            notificationService.sendPlainTextEmail(initiatorPrincipal.getEmail(),
                    initiatorPrincipal.getFullName(), "Project Proposal Submission Confirmation",
                    "Your prposal has been initiated. Please complete all the sections.");
        } catch (Exception ex) {
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
        if (p.getJvUser() != null)
            dto.setJv(new UserLookupItem(p.getJvUser().getId(), p.getJvUser().getFullName()));
        else
            dto.setJv(null);
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
            section.setOrderNum(qs.getSectionRef().getOrderNum());
            section.setReassignmentComments(qs.getReassignmentComments());

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

            preAppItem.setCommentsByPo(p.getPreAppraisal().getCommentsByPo());

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
            eaItem.setDecisionByDm(e.getDecisionByDm());

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
                if (s.getSectionRef().getOrderNum() != null) {
                    item.setOrderNum(s.getSectionRef().getOrderNum());
                }
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
                    review.setPoComments(r.getPoComments());
                    review.setStartDate(r.getStartDate());
                    review.setEndDate(r.getEndDate());
                    review.setId(r.getId());
                    review.setStatus(r.getStatus());
                    giaItem.addReview(review);
                });
            }

            List<GrantImplementationAgreementReview> array;
            array = grantImplementationAgreementReviewRepository.findRequestsByProposalId(p.getId());
            if (array != null && array.size() > 0) {
                array.forEach(r -> {
                    GiaReviewItem review = new GiaReviewItem();
                    review.setAssigned(r.getAssignee().getId().equals(userId));
                    review.setAssignee(new UserLookupItem(r.getAssignee().getId(), r.getAssignee().getFullName()));
                    review.setComments(r.getComments());
                    review.setPoComments(r.getPoComments());
                    review.setStartDate(r.getStartDate());
                    review.setEndDate(r.getEndDate());
                    review.setId(r.getId());
                    review.setStatus(r.getStatus());
                    giaItem.addReviewsHistory(review);
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


//		if (p.getOfferLetter() != null) {
//			OfferLetterItem dtoOl = new OfferLetterItem();
//			dtoOl.setId(p.getOfferLetter().getId());
//			dtoOl.setStatus(p.getOfferLetter().getStatus());
//			dtoOl.setExpiryDate(p.getOfferLetter().getExpiryDate());
//			dtoOl.setFipMarkingStatus(p.getOfferLetter().getFipMarkingStatus());
//			dtoOl.setGmMarkingStatus(p.getOfferLetter().getGmMarkingStatus());
//			dtoOl.setGmMarkingStatus(p.getOfferLetter().getGM_comments());
//			dtoOl.setGmMarkingStatus(p.getOfferLetter().getFIP_comments());
//			dtoOl.setGmMarkingStatus(p.getOfferLetter().getData());
//			dtoOl.setGmMarkingStatus(p.getOfferLetter().getStage());
//			dtoOl.setGmResponse(p.getOfferLetter().getGM_response());
//			dtoOl.setFipResponse(p.getOfferLetter().getFIP_response());
//			dto.setOfferLetter(dtoOl);
//		}

        dto.setProposalName(p.getName());

        if (p.getTpv() != null && p.getTpv().getStatus() != null)
            dto.setTpvStatus(p.getTpv().getStatus());
        if (p.getPc() != null && p.getPc().getStatus() != null)
            dto.setPcStatus(p.getPc().getStatus());

        return dto;
    }

    public List<ProjectProposalListItem> getProjectProposalRequests(AuthPrincipal currentUser, ProcessStatus status) throws IOException {

        List<ProjectProposal> props = new ArrayList<ProjectProposal>();
        List<ProjectProposal> giaprops = new ArrayList<ProjectProposal>();
        List<ProjectProposal> qprprops = new ArrayList<ProjectProposal>();
        List<ProjectProposal> disbursmentprops = new ArrayList<ProjectProposal>();

        com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
                .findById(ProcessStatus.GIA.getPersistenceValue())
                .orElseThrow(() -> new ValidationException("GIA Process is not defined."));
        com.ndrmf.setting.model.ProcessType qprProcessType = processTypeRepo
                .findById(ProcessStatus.QPR.getPersistenceValue())
                .orElseThrow(() -> new ValidationException("QPR Process is not defined."));
        com.ndrmf.setting.model.ProcessType disbursmentProcessType = processTypeRepo
                .findById(ProcessType.DISBURSEMENT.toString())
                .orElseThrow(() -> new ValidationException("Disbursment Process is not defined."));

        if (currentUser.getUserId().equals(giaProcessType.getOwner().getId())
                || currentUser.getUserId().equals(qprProcessType.getOwner().getId()) ||
                currentUser.getUserId().equals(disbursmentProcessType.getOwner().getId())) {
//			giaprops.addAll(projProposalRepo.findAllRequestsByStatus(ProcessStatus.GIA.getPersistenceValue()));
            giaprops.addAll(projProposalRepo.findAllGiaAndAboveRequests());
        }

        if (currentUser.getRoles() != null && (currentUser.getRoles().contains(SystemRoles.GM)
                || currentUser.getRoles().contains(SystemRoles.CEO))) {
            if (status == null) {
                props.addAll(projProposalRepo.findAll());
            } else {
                props.addAll(projProposalRepo.findAllRequestsByStatus(status.getPersistenceValue()));
            }
        }
        else if (currentUser.getRoles() != null && (currentUser.getRoles().contains(SystemRoles.MOBILE_USER))){
            qprprops.addAll(projProposalRepo.findAllRequestsByStatus(ProcessStatus.GIA_CHECKLIST.getPersistenceValue()));

        }
        else {
            if (status == null) {
                props.addAll(projProposalRepo.findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(currentUser.getUserId()));
            } else {
                props.addAll(projProposalRepo.findRequestsForOwnerOrInitiatorOrDMPAMOrSMEByStatus(currentUser.getUserId(),
                        status.getPersistenceValue()));
            }
        }


        props.addAll(giaprops);
		props.addAll(qprprops);
//		props.addAll(disbursmentprops);
        List<ProjectProposalListItem> dtos = new ArrayList<>();
        props.forEach(q -> {
            ProjectProposalListItem ppli = new ProjectProposalListItem(q.getId(), q.getName(), q.getThematicArea().getName(),
                    q.getInitiatedBy().getFullName(), q.getCreatedDate(), q.getStatus());
            if (q.getInitiatedBy().getRoles().stream().anyMatch(r -> r.getOrg().getId() == SystemRoles.ORG_GOVT_ID)) {
                ppli.setGov(true);
            }
            dtos.add(ppli);
        });
//        System.out.println("SYSTEM ROLE MOBILE" + currentUser.getRoles() + currentUser.getRoles().contains(SystemRoles.MOBILE_USER));
        return dtos;
    }


    public void submitProposalSection(AuthPrincipal user, UUID proposalId, ProjectProposalSectionRequest body,
                                      FormAction action) {
        ProjectProposal p = projProposalRepo.findById(proposalId)
                .orElseThrow(() -> new ValidationException("Invalid request ID"));

//		if (!p.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())
//				&& !p.getStatus().equals(ProcessStatus.REASSIGNED.getPersistenceValue())) {
//			throw new ValidationException("Request is already: " + p.getStatus());
//		}

        ProjectProposalSection section = p.getSections().stream().filter(s -> s.getId().equals(body.getId())).findAny()
                .orElseThrow(() -> new ValidationException("Invalid ID"));

        section.setData(body.getData());

        if (p.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())) {
            if (action.equals(FormAction.SAVE)) {
                p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
            } else if (action.equals(FormAction.SUBMIT)) {
                p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
            }
        }

        if (section.getReassignmentStatus() != null
                && section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
            section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
        }

        projProposalRepo.save(p);

        if (action == FormAction.SUBMIT) {
            // TODO raise event
            // eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));
            try {
                notificationService.sendPlainTextEmail(
                        p.getInitiatedBy().getEmail(),
                        p.getInitiatedBy().getFullName(),
                        "Project " + p.getName() + " Proposal Request is Under Review at NDRMF",
                        "Your project proposal request for project " + p.getName() + " is in Under Review stage " +
                                "please visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                + " to review your request."
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                notificationService.sendPlainTextEmail(
                        p.getProcessOwner().getEmail(),
                        p.getProcessOwner().getFullName(),
                        "New Project Proposal request submitted at NDRMF",
                        p.getInitiatedBy().getFullName() +
                                ", has submiited a new project proposal request  with the name " + p.getName() +
                                "\nplease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                + " to review and process the application."
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        appraisal.setCommentsByPo(body.getComments());
        appraisal.setStatus(ProcessStatus.PENDING.getPersistenceValue());

        proposal.setPreAppraisal(appraisal);
        proposal.setStatus(ProcessStatus.PRELIMINARY_APPRAISAL.getPersistenceValue());

        try {
            notificationService.sendPlainTextEmail(
                    dmPAM.getEmail(),
                    dmPAM.getFullName(),
                    "Assigned Pre-appraisal on Project Proposal" + proposal.getName() + " at NDRMF",
                    proposal.getProcessOwner().getFullName() +
                            ", has assigned you the primary apprasisal on project proposal " + proposal.getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/fill-primary-appraisal/" + proposal.getId()
                            + " to review and process the request." +
                            "\nStart Date: " + body.getStartDate() +
                            "\nEnd Date: " + body.getEndDate() +
                            "\nComments from NDRMF: " + body.getComments()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        try {
            notificationService.sendPlainTextEmail(
                    proposal.getProcessOwner().getEmail(),
                    proposal.getProcessOwner().getFullName(),
                    "Pre-appraisal submitted by DM-PAM on Project Proposal " + proposal.getName() + " at NDRMF",
                    dmPAM.getFullName() +
                            ", has submitted the pre-appraisal on project proposal " + proposal.getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/view-primary-appraisal/" + proposal.getId()
                            + " to review and process the request."
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

            try {
                notificationService.sendPlainTextEmail(
                        t.getSection().getSme().getEmail(),
                        t.getSection().getSme().getFullName(),
                        "Extended appraisal assigned on Project Proposal " + proposal.getName() + " at NDRMF",
                        proposal.getProcessOwner().getFullName() +
                                ", has assigned you the extended apprasisal on project proposal " + proposal.getName() +
                                ".\nPlease visit http://ndrmfdev.herokuapp.com/add-extended-appraisal-form/" + proposal.getId()
                                + " to review and process the request." +
                                "\nStart Date: " + body.getStartDate() +
                                "\nEnd Date: " + body.getEndDate() +
                                "\nComments from NDRMF: " + body.getComments()
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        proposal.setExtendedAppraisal(e);

        proposal.setStatus(ProcessStatus.EXTENDED_APPRAISAL.getPersistenceValue());

        try {
            proposal = projProposalRepo.save(proposal);
            e = proposal.getExtendedAppraisal();
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while commencing Extended Appraisal", ex);
        }

        try {
            notificationService.sendPlainTextEmail(
                    dmPAM.getEmail(),
                    dmPAM.getFullName(),
                    "Extended appraisal assigned on Project Proposal " + proposal.getName() + " at NDRMF",
                    proposal.getProcessOwner().getFullName() +
                            ", has assigned you the extended apprasisal on project proposal " + proposal.getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/add-extended-appraisal-form/" + proposal.getId()
                            + " to review and process the request." +
                            "\nStart Date: " + body.getStartDate() +
                            "\nEnd Date: " + body.getEndDate() +
                            "\nComments from NDRMF: " + body.getComments()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ExtendedAppraisalItem dto = new ExtendedAppraisalItem();
        dto.setId(e.getId());
        dto.setAssigned(e.getAssignee().getId().equals(processOwnerId));
        dto.setAssignee(new UserLookupItem(e.getAssignee().getId(), e.getAssignee().getFullName()));
        dto.setComments(e.getComments());
        dto.setEndDate(e.getEndDate());
        dto.setStartDate(e.getStartDate());
        dto.setStatus(e.getStatus());

        dto.setDecisionByDm(e.getDecisionByDm());

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

            User dmPAM = userService.getDMPAM()
                    .orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));

            try {
                notificationService.sendPlainTextEmail(
                        dmPAM.getEmail(),
                        dmPAM.getFullName(),
                        "Extended appraisal completed on Project Proposal " + a.getProposalRef().getName() + " at NDRMF",
                        a.getProposalRef().getProcessOwner().getFullName() +
                                ", all sections has been submitted on extended apprasisal for project proposal " + a.getProposalRef().getName() +
                                ".\nPlease visit http://ndrmfdev.herokuapp.com/add-extended-appraisal-form/" + a.getProposalRef().getId()
                                + " to review and process the request."
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Transactional
    public void assignExtendedAppraisalSection(UUID userId, UUID extAppraisalId, AssignExtendedAppraisalSectionRequest body) {
        ExtendedAppraisal a = extAppRepo.findById(extAppraisalId)
                .orElseThrow(() -> new ValidationException("Invalid Extended Appraisal ID"));

        ExtendedAppraisalSection section = a.getSections().stream().filter(s -> s.getId().equals(body.getId()))
                .findAny().orElseThrow(() -> new ValidationException("Invalid ID"));
        section.setStatus(ProcessStatus.PENDING.getPersistenceValue());

        try {
            notificationService.sendPlainTextEmail(
                    section.getSme().getEmail(),
                    section.getSme().getFullName(),
                    "Extended appraisal section re-assigned on Project Proposal " + section.getExtendedAppraisalRef().getProposalRef().getName() + " at NDRMF",
                    section.getSectionRef().getName() +
                            ", has been re-assigned to you on the extended apprasisal on project proposal " + section.getExtendedAppraisalRef().getProposalRef().getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/add-extended-appraisal-form/" + section.getExtendedAppraisalRef().getProposalRef().getId()
                            + " to review and process the request."
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public void extendedAppraisalDecisionByDm(AuthPrincipal user, UUID extAppraisalId) {
        ExtendedAppraisal a = extAppRepo.findById(extAppraisalId)
                .orElseThrow(() -> new ValidationException("Invalid Extended Appraisal ID"));
        a.setDecisionByDm(ProcessStatus.APPROVED.getPersistenceValue());
        try {
            notificationService.sendPlainTextEmail(
                    a.getProposalRef().getProcessOwner().getEmail(),
                    a.getProposalRef().getProcessOwner().getFullName(),
                    "Extended appraisal approved on Project Proposal " + a.getProposalRef().getName() + " at NDRMF",
                    user.getFullName() +
                            ", has approved the extended apprasisal on project proposal " + a.getProposalRef().getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/add-extended-appraisal-form/" + a.getProposalRef().getId()
                            + " to review and process the request."
            );
        } catch (Exception ex) {
            ex.printStackTrace();
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
                review.setPoComments(body.getPoComments());
                review.setStartDate(body.getStartDate());
                review.setEndDate(body.getEndDate());
                review.setProposalRef(proposal);
                gia.addReview(review);
                try {
                    notificationService.sendPlainTextEmail(
                            userRepo.getOne(rv).getEmail(),
                            userRepo.getOne(rv).getFullName(),
                            "GIA remarks pending on Project Proposal " + proposal.getName() + " at NDRMF",
                            giaProcessType.getOwner().getFullName() +
                                    ", has assigned you to give your remarks on GIA for project proposal " + proposal.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/gia-appraisal/" + proposal.getId()
                                    + " to review the request and submit your remarks."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            gia.setSubStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());


        }
        gia.setStatus(ProcessStatus.PENDING.getPersistenceValue());
        proposal.setGia(gia);
        proposal.setStatus(ProcessStatus.GIA.getPersistenceValue());

        try {
            notificationService.sendPlainTextEmail(
                    proposal.getProcessOwner().getEmail(),
                    proposal.getProcessOwner().getFullName(),
                    "GIA created on Project Proposal " + proposal.getName() + " at NDRMF",
                    giaProcessType.getOwner().getFullName() +
                            ", has created the GIA on project proposal " + proposal.getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/gia-appraisal/" + proposal.getId()
                            + " to review and process the request."
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

            com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
                    .findById(ProcessStatus.GIA.getPersistenceValue())
                    .orElseThrow(() -> new ValidationException("GIA Process is not defined."));

            try {
                notificationService.sendPlainTextEmail(
                        giaProcessType.getOwner().getEmail(),
                        giaProcessType.getOwner().getFullName(),
                        "GIA reviews completed on Project Proposal " + proposal.getName() + " at NDRMF",
                        "GIA on project proposal " + proposal.getName() + " has been reviewed completely." +
                                ".\nPlease visit http://ndrmfdev.herokuapp.com/gia-appraisal/" + proposal.getId()
                                + " to review and process the request."
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
                && proposal.getGia().getSubStatus() != null &&
                proposal.getGia().getSubStatus().equals(ProcessStatus.REVIEW_PENDING.getPersistenceValue())) {
            throw new ValidationException("Cannot change Status. There are still Pending Reviews for GIA");
        }

        proposal.getGia().setStatus(status.getPersistenceValue());

        if (status.equals(ProcessStatus.APPROVED)) {
            GIAChecklist checklist = new GIAChecklist();
            checklist.setDeadline(checklistDeadline);
            checklist.setStatus(ProcessStatus.PENDING.getPersistenceValue());

            proposal.setGiaChecklist(checklist);
            proposal.setStatus(ProcessStatus.GIA_CHECKLIST.getPersistenceValue());

            try {
                notificationService.sendPlainTextEmail(
                        proposal.getInitiatedBy().getEmail(),
                        proposal.getInitiatedBy().getFullName(),
                        "Submit GIA Checklist on Project Proposal " + proposal.getName() + " at NDRMF",
                        "GIA has been approved for project proposal " + proposal.getName() +
                                ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + proposal.getId()
                                + " to review and process the request.\n" +
                                "Due Date: " + checklistDeadline + "\n"
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

        try {
            notificationService.sendPlainTextEmail(
                    proposal.getProcessOwner().getEmail(),
                    proposal.getProcessOwner().getFullName(),
                    "GIA Checklist submitted on Project Proposal " + proposal.getName() + " at NDRMF",
                    proposal.getInitiatedBy().getFullName() + " has submitted GIA Checklist for project proposal " + proposal.getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + proposal.getId()
                            + " to review and process the request."
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            notificationService.sendPlainTextEmail(
                    proposal.getInitiatedBy().getEmail(),
                    proposal.getInitiatedBy().getFullName(),
                    "GIA Checklist submitted on Project Proposal " + proposal.getName() + " at NDRMF",
                    "You have submitted GIA Checklist for project proposal " + proposal.getName() +
                            ".\nYou can now submit the sub project document schemes." +
                            "\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + proposal.getId()
                            + " to review and process the request."
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        try {
            notificationService.sendPlainTextEmail(
                    section.getSme().getEmail(),
                    section.getSme().getFullName(),
                    "Submit your reviews on Project Proposal " + section.getProposalRef().getName() + " at NDRMF",
                    "Please submit your remarks on " + section.getName() +
                            ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + section.getProposalRef().getId()
                            + " to review and submit your remarks." +
                            "\nStart Date: " + body.getStartDate() +
                            "\nEnd Date: " + body.getEndDate() +
                            "\nComments from NDRMF: " + body.getComments()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // TODO - trigger notification event
    }

    @Transactional
    public void updateProposalStatus(UUID proposalId, AuthPrincipal user, ProcessStatus status, ProcessStatus subStatus, OfferLetterUpdateRequest body) {

        User u = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ValidationException("Invalid request ID"));

        Organisation userOrg = u.getOrg();

        int user_org = 0;    //1 for NDRMF, 2 for FIP, 3 for Govt_fip

        ProjectProposal p = projProposalRepo.findById(proposalId)
                .orElseThrow(() -> new ValidationException("Invalid request ID"));

        if (p.getStatus().equals(ProcessStatus.PRELIMINARY_APPRAISAL.getPersistenceValue())) {
            PreliminaryAppraisal app = p.getPreAppraisal();
//			app.setIsMarkedTo(status.getPersistenceValue());
//			app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());

//			if (status.equals(ProcessStatus.MARKED_TO_GM) || status.equals(ProcessStatus.MARKED_TO_CEO)) {
            app.setIsMarkedTo(status.getPersistenceValue());
            app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());

            User assignedUser;

            if (status.equals(ProcessStatus.MARKED_TO_GM)) {
                assignedUser = userRepo.findActiveUsersForRole(SystemRoles.GM).get(0);
                try {
                    notificationService.sendPlainTextEmail(
                            assignedUser.getEmail(),
                            assignedUser.getFullName(),
                            "Pre-appraisal remarks on Project Proposal " + p.getName() + " at NDRMF",
                            "Please submit your remarks on " + p.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and submit your remarks."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (status.equals(ProcessStatus.MARKED_TO_CEO)) {
                assignedUser = userRepo.findActiveUsersForRole(SystemRoles.CEO).get(0);
                try {
                    notificationService.sendPlainTextEmail(
                            assignedUser.getEmail(),
                            assignedUser.getFullName(),
                            "Pre-appraisal remarks on Project Proposal " + p.getName() + " at NDRMF",
                            "Please submit your remarks on " + p.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and submit your remarks."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

//			}
            else if (status.equals(ProcessStatus.APPROVED)) {
                app.setSubStatus(status.getPersistenceValue());
                try {
                    notificationService.sendPlainTextEmail(
                            p.getProcessOwner().getEmail(),
                            p.getProcessOwner().getFullName(),
                            "Pre-appraisal " + status.getPersistenceValue() + " on project" + p.getName() + " at NDRMF",
                            "Review the board remarks on " + p.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and submit your remarks."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (status.equals(ProcessStatus.REJECTED)) {
                app.setSubStatus(status.getPersistenceValue());
                try {
                    notificationService.sendPlainTextEmail(
                            p.getProcessOwner().getEmail(),
                            p.getProcessOwner().getFullName(),
                            "Pre-appraisal " + status.getPersistenceValue() + " on project" + p.getName() + " at NDRMF",
                            "Review the board remarks on " + p.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and submit your remarks."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                p.setStatus(status.getPersistenceValue());
            }
//			p.setStatus(status.getPersistenceValue());
        } else if (p.getStatus().equals(ProcessStatus.EXTENDED_APPRAISAL.getPersistenceValue())) {
            ExtendedAppraisal app = p.getExtendedAppraisal();

            if (status.equals(ProcessStatus.MARKED_TO_GM) || status.equals(ProcessStatus.MARKED_TO_CEO)) {
                app.setIsMarkedTo(status.getPersistenceValue());
                app.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
                User assignedUser;
                if (status.equals(ProcessStatus.MARKED_TO_GM)) {
                    assignedUser = userRepo.findActiveUsersForRole(SystemRoles.GM).get(0);
                    try {
                        notificationService.sendPlainTextEmail(
                                assignedUser.getEmail(),
                                assignedUser.getFullName(),
                                "Pre-appraisal remarks on Project Proposal " + p.getName() + " at NDRMF",
                                "Please submit your remarks on " + p.getName() +
                                        ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                        + " to review and submit your remarks."
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (status.equals(ProcessStatus.MARKED_TO_CEO)) {
                    assignedUser = userRepo.findActiveUsersForRole(SystemRoles.CEO).get(0);
                    try {
                        notificationService.sendPlainTextEmail(
                                assignedUser.getEmail(),
                                assignedUser.getFullName(),
                                "Pre-appraisal remarks remarks on Project Proposal " + p.getName() + " at NDRMF",
                                "Please submit your remarks on " + p.getName() +
                                        ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                        + " to review and submit your remarks."
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (status.equals(ProcessStatus.APPROVED) || status.equals(ProcessStatus.REJECTED)) {
                app.setSubStatus(status.getPersistenceValue());
                try {
                    notificationService.sendPlainTextEmail(
                            p.getProcessOwner().getEmail(),
                            p.getProcessOwner().getFullName(),
                            "Extended-appraisal " + status.getPersistenceValue() + " on project" + p.getName() + " at NDRMF",
                            "Review the board remarks on " + p.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and submit your remarks."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                p.setStatus(status.getPersistenceValue());
            }
        } else if (
                p.getStatus().equals(ProcessStatus.OFFER_LETTER.getPersistenceValue())
                        && status == ProcessStatus.OFFER_LETTER
        ) {
            System.out.println("OFFER LETTER CALLED");
            if (body != null) {
                p.getOfferLetter().setGmMarkingStatus(body.getGmStatus());
                p.getOfferLetter().setFipMarkingStatus(body.getFipStatus());
                p.getOfferLetter().setFIP_comments(body.getFipComments());
                p.getOfferLetter().setGM_comments(body.getGmComments());
                p.getOfferLetter().setExpiryDate(body.getExpiryDate());
                p.getOfferLetter().setFIP_response(body.getFipResponse());
                p.getOfferLetter().setGM_response(body.getGmResponse());
            }
            if (body.getGmResponse() != null) {
                try {
                    notificationService.sendPlainTextEmail(
                            p.getProcessOwner().getEmail(),
                            p.getProcessOwner().getFullName(),
                            "Offer letter on project " + p.getName() + " has been " + body.getGmResponse(),
                            "Offer letter on project " + p.getName() + " is" + body.getGmResponse() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and proceed to next step."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (body.getFipStatus().equals(ProcessStatus.PENDING.getPersistenceValue())) {
                try {
                    notificationService.sendPlainTextEmail(
                            p.getInitiatedBy().getEmail(),
                            p.getInitiatedBy().getFullName(),
                            "Offer letter on project " + p.getName() + " has been generated.",
                            "Offer letter on project " + p.getName() + " is ready for signing" +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and accept the offer letter.\n" +
                                    "Expiry Date: " + body.getExpiryDate() +
                                    "\nComments from NDRMF: " + body.getFipComments()
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (body.getFipResponse() != null) {
                try {
                    notificationService.sendPlainTextEmail(
                            p.getProcessOwner().getEmail(),
                            p.getProcessOwner().getFullName(),
                            "Offer letter on project " + p.getName() + " has been " + body.getFipResponse() + " by FIP.",
                            "Offer letter on project " + p.getName() + " is" + body.getFipResponse() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and proceed to next step."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (status == ProcessStatus.OFFER_LETTER && !p.getStatus().equals(ProcessStatus.OFFER_LETTER.getPersistenceValue())) {
            OfferLetter oL = new OfferLetter();
            oL.setStatus(subStatus.getPersistenceValue());
            oL.setProposalRef(p);
            p.setOfferLetter(oL);
            p.setStatus(ProcessStatus.OFFER_LETTER.getPersistenceValue());
            oL = offerLetterRepo.save(oL);

            User assignedUser = userRepo.findActiveUsersForRole(SystemRoles.GM).get(0);

            try {
                notificationService.sendPlainTextEmail(
                        assignedUser.getEmail(),
                        assignedUser.getFullName(),
                        "Offer letter on project " + p.getName() + " is assigned at NDRMF",
                        p.getProcessOwner().getFullName() + " assigned you offer letter on " + p.getName() +
                                ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                + " to review and submit your remarks." +
                                "\nExpiry Date: " + body.getExpiryDate() +
                                "\nComments from NDRMF: " + body.getGmComments()
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            p.setStatus(status.getPersistenceValue());
            if (status.getPersistenceValue().equals(ProcessStatus.GIA.getPersistenceValue())) {
                com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
                        .findById(ProcessStatus.GIA.getPersistenceValue())
                        .orElseThrow(() -> new ValidationException("GIA Process is not defined."));
                try {
                    notificationService.sendPlainTextEmail(
                            giaProcessType.getOwner().getEmail(),
                            giaProcessType.getOwner().getFullName(),
                            "GIA is enabled on project " + p.getName() + " at NDRMF",
                            p.getProcessOwner().getFullName() + " assigned you to submit GIA on project " + p.getName() +
                                    ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                    + " to review and prepare GIA."
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Transactional
    public String addProposalAttachment(
            UUID proposalId,
            UUID userId,
            ProcessStatus stage,
            MultipartFile file) throws IOException {
        ProjectProposal p = projProposalRepo.findById(proposalId)
                .orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

        rFile persistedFile = fileStoreService.saveFile(file, userId);

        ProjectProposalAttachment attachment = new ProjectProposalAttachment();
        attachment.setFileRef(persistedFile);
        attachment.setPicByte(compressBytes(file.getBytes()));
        attachment.setStage(stage.getPersistenceValue());


        p.addAttachement(attachment);
        projectProposalAttachmentRepo.save(attachment);

        if (stage == ProcessStatus.OFFER_LETTER) {
            OfferLetter oL = p.getOfferLetter();
//			System.out.println(persistedFile);
//			if (persistedFile != null)
            oL.setFileRef(persistedFile);
//			offerLetterRepo.save(oL);
            p.setOfferLetter(oL);
        }

        if (stage == ProcessStatus.UPLOAD_PC1) {
            p.setStatus(ProcessStatus.UPLOAD_PDRMC.getPersistenceValue());
        } else if (stage == ProcessStatus.UPLOAD_PDRMC) {
            p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
        }

//		projProposalRepo.save(p);

        return persistedFile.getPath();
    }

    @Transactional
    public String updateProposalOfferLetterStatus(UUID proposalId, UUID userId, ProcessStatus status) throws IOException {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ValidationException("Invalid User ID"));
        String response = "Successful";
        ProjectProposal p = projProposalRepo.findById(proposalId)
                .orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

        List<ProjectProposalAttachment> attachments =
                projectProposalAttachmentRepo.findProjectProposalAttachmentByProposalIdAndStage(proposalId,
                        ProcessStatus.OFFER_LETTER.getPersistenceValue());
        if (attachments.size() == 0) {
            return "No offer letter file attached.";
        }
        ProjectProposalAttachment attachment = attachments.get(0);
        if (p.getStatus() == ProcessStatus.OFFER_LETTER.getPersistenceValue() &&
                (attachment.getGmMarkingStatus() == ProcessStatus.PENDING.getPersistenceValue() ||
                        attachment.getGmMarkingStatus() == ProcessStatus.UNDER_REVIEW.getPersistenceValue())) {
            attachment.setGmMarkingStatus(ProcessStatus.APPROVED.getPersistenceValue());
            attachment.setFipMarkingStatus(ProcessStatus.PENDING.getPersistenceValue());
        } else if (p.getStatus() == ProcessStatus.OFFER_LETTER.getPersistenceValue() &&
                attachment.getGmMarkingStatus() == ProcessStatus.PENDING.getPersistenceValue()) {

        }

        return response;
    }

    @Transactional
    public List<Object> readProposalAttachmentByStage(UUID proposalId, ProcessStatus stage) {

        List<Object> fileData = projectProposalAttachmentRepo.findAttachedFileIdsByProposalIdAndStage(proposalId, stage.getPersistenceValue());
        return fileData;
    }

    @Transactional
    public List<ProjectProposalAttachment> readProposalAttachmentsByProposalId(UUID proposalId) {
        List<ProjectProposalAttachment> fileData = projectProposalAttachmentRepo.findAttachedFileIdsByProposalId(proposalId);
        return fileData;
    }

    @Transactional
    public List getAttachmentsByFileNameAndPath(String fileName, String filePath) {
        List<ProjectProposalAttachment> fileData;
        fileData = projectProposalAttachmentRepo.findAttachedFileByfileNameAndPath(fileName, filePath);
        return fileData;

    }

    @Transactional
    public void reassignProposalToFIP(UUID proposalId, UUID userId, Set<UUID> sectionIds, String comments) {
        ProjectProposal p = projProposalRepo.findById(proposalId)
                .orElseThrow(() -> new ValidationException("Invalid Proposal ID"));

        sectionIds.forEach(sId -> {
            ProjectProposalSection section = p.getSections()
                    .stream()
                    .filter(s -> s.getId().equals(sId))
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("Invalid Section ID"));

            section.setReassignmentStatus(ProcessStatus.PENDING.getPersistenceValue());
            section.setReassignmentComments(comments);

            try {
                notificationService.sendPlainTextEmail(
                        p.getInitiatedBy().getEmail(),
                        p.getInitiatedBy().getFullName(),
                        "Project Proposal " + p.getName() + " section(s) reassigned at NDRMF",
                        p.getProcessOwner().getFullName() + " has reassigned you section " + section.getName() +
                                ".\nPlease visit http://ndrmfdev.herokuapp.com/project-details/" + p.getId()
                                + " to review and submit the section again." +
                                "\nComments from NDRMF: " + comments
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        });
    }

    public OfferLetterItem getOfferLetter(AuthPrincipal principal, UUID proposalId) {
        OfferLetter oL = offerLetterRepo.findAllRequestsByProposalId(proposalId);

        OfferLetterItem dto = new OfferLetterItem();
        if (oL != null) {
            dto.setId(oL.getId());
            dto.setData(oL.getData());
            dto.setExpiryDate(oL.getExpiryDate());
            dto.setFIP_comments(oL.getFIP_comments());
            dto.setGM_comments(oL.getGM_comments());
            dto.setFipMarkingStatus(oL.getFipMarkingStatus());
            dto.setGmMarkingStatus(oL.getGmMarkingStatus());
            dto.setGmResponse(oL.getGM_response());
            dto.setFipResponse(oL.getFIP_response());
            dto.setStage(oL.getStage());
            dto.setStatus(oL.getStatus());
            dto.setProposalRefId(oL.getProposalRef().getId());
//			dto.setFileRef(oL.getFileRef());
            return dto;
        }
        return null;
    }

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