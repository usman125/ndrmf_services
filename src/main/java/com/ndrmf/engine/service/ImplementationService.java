package com.ndrmf.engine.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import javax.transaction.Transactional;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.model.*;
import com.ndrmf.util.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.SubProjectDocumentItem.SubProjectDocumentSectionItem;

import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.SubProjectDocumentRepository;
import com.ndrmf.engine.repository.SubProjectDocumentSectionRepository;
import com.ndrmf.engine.repository.SubProjectDocumentDmPamTasksRepository;
import com.ndrmf.engine.repository.SubProjectDocumentTasksRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ImplementationService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private SubProjectDocumentRepository subProjectRepo;
	@Autowired private SubProjectDocumentSectionRepository subProjectSectionRepo;
	@Autowired private SubProjectDocumentDmPamTasksRepository subProjectDmpamTasksRepo;
	@Autowired private SubProjectDocumentTasksRepository subProjectTasksRepo;
	@Autowired private UserRepository userRepository;
	@Autowired private SubProjectDocumentDmPamTasksRepository spdDmPamRepo;
	@Autowired
	private ObjectMapper objectMapper;
	/*
	 * After signing GIA, FIP has to submit sub project documents (Template shared by PAM)
	 * within 120 days. The system provides triggers at 90, 100 and then 120 days about the
	 * submission of the documents.
	 * 
	 * This method will iterate through all proposals -> PIP and commence subproject document phase
	 * for proposals wherever applicable
	 */
	//@Scheduled(cron = "0 0 1 * * ?")
	public void commenceSubProjectDocument(UUID proposalId, CommenceSubProjectDocumentBody body) {
		
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.SUB_PROJECT_DOCUMENT.name());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for SUB_PROJECT_DOCUMENT process");
		}
		
		com.ndrmf.setting.model.ProcessType processType = processTypeRepo.findById(ProcessType.SUB_PROJECT_DOCUMENT.name())
				.orElseThrow(() -> new RuntimeException("Process Owner not defined for SUB_PROJECT_DOCUMENT"));
		
		SubProjectDocument doc = new SubProjectDocument();
		doc.setProcessOwner(processType.getOwner());
//		doc.setStartDate(new Date());
//		doc.setEndDate(LocalDate.now().plusDays(30));
		doc.setDocName(body.getDocName());
		doc.setDocNumber(body.getDocNumber());
		doc.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		doc.setProposalRef(p);
		
		for (SectionTemplate t : sts) {
			SubProjectDocumentSection s = new SubProjectDocumentSection();
			s.setSme(t.getSection().getSme());
			s.setTemplate(t.getTemplate());
			s.setTemplateType(t.getTemplateType());
			s.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			s.setSectionRef(t.getSection());

			doc.addSection(s);
		}
		
		subProjectRepo.save(doc);
	}
	
	public List<SubProjectDocumentListItem> getPendingSubProjectDocuments(UUID userId) {
//		List<SubProjectDocument> docs = subProjectRepo.getSubProjectsForFIPByStatus(userId, ProcessStatus.PENDING.getPersistenceValue());
		List<SubProjectDocument> docs = subProjectRepo.getSubProjectsForFIPByStatus(userId);
		
		List<SubProjectDocumentListItem> dtos = new ArrayList<>();
		docs.forEach(d -> {
			SubProjectDocumentListItem item = new SubProjectDocumentListItem();
			item.setId(d.getId());
			item.setProposalName(d.getProposalRef().getName());
			item.setStartDate(d.getStartDate());
			item.setEndDate(d.getEndDate());
			item.setStatus(d.getStatus());
			item.setDocName(d.getDocName());
			item.setDocNumber(d.getDocNumber());
			dtos.add(item);
		});
		
		return dtos;
	}
	
	public List<SubProjectDocumentListItem> getSubProjectDocuments(UUID userId) {

		List<SubProjectDocument> docs = subProjectRepo.getSubProjectsForPOOrReviewer(userId, Arrays.asList(ProcessStatus.DRAFT.getPersistenceValue()));

		List<SubProjectDocumentListItem> dtos = new ArrayList<>();
		docs.forEach(d -> {
			SubProjectDocumentListItem item = new SubProjectDocumentListItem();
			item.setId(d.getId());
			item.setProposalName(d.getProposalRef().getName());
			item.setStartDate(d.getStartDate());
			item.setEndDate(d.getEndDate());
			item.setStatus(d.getStatus());
			item.setDocName(d.getDocName());
			item.setDocNumber(d.getDocNumber());
			dtos.add(item);
		});
		
		return dtos;
	}
	
	public SubProjectDocumentItem getSubProjectDocument(UUID id, UUID userId) {
		SubProjectDocument doc = subProjectRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid ID"));

		SubProjectDocumentItem dto = new SubProjectDocumentItem();

		dto.setStartDate(doc.getStartDate());
		dto.setEndDate(doc.getEndDate());
		dto.setStatus(doc.getStatus());

		dto.setDocName(doc.getDocName());
		dto.setDocNumber(doc.getDocNumber());

		doc.getSections().forEach(s -> {
			SubProjectDocumentSectionItem item = new SubProjectDocumentSectionItem();
			item.setId(s.getId());
			item.setAssigned(userId.equals(s.getSme().getId()));
			item.setComments(s.getComments());
			item.setData(s.getData());
			item.setSme(new UserLookupItem(s.getSme().getId(), s.getSme().getFullName()));
			item.setStatus(s.getStatus());
			item.setTemplate(s.getTemplate());
			item.setTemplateType(s.getTemplateType());
			item.setReviewStatus(s.getReviewStatus());
			item.setReviewCompletedOn(s.getReviewCompletedOn());
			item.setSectionName(s.getSectionRef().getName());
			item.setReassignmentStatus(s.getReassignmentStatus());
			item.setReassignmentComments(s.getReassignmentComments());
			dto.addSection(item);
		});
		List<SubProjectDocumentDmPamTasks> spdmptl = spdDmPamRepo.getSubProjectDocumentDmPamTasksBySpdId(doc.getId());
		if (spdmptl.size() > 0){
			spdmptl.stream().forEach(c -> {
				SubProjectDocumentDmPamTasksItem spddmpamli = new SubProjectDocumentDmPamTasksItem();
				spddmpamli.setAssignee(new UserLookupItem(c.getAssignee().getId(), c.getAssignee().getFullName()));
				spddmpamli.setAssigneedBy(new UserLookupItem(c.getAssigneedBy().getId(), c.getAssigneedBy().getFullName()));
				spddmpamli.setComments(c.getComments());
				spddmpamli.setSubStatus(c.getSubStatus());
				spddmpamli.setStatus(c.getStatus());
				spddmpamli.setCompletedOn(c.getCompletedOn());
				spddmpamli.setStartDate(c.getStartDate());
				spddmpamli.setEndDate(c.getEndDate());
				spddmpamli.setId(c.getId());
				subProjectTasksRepo.getSubProjectDocumentTasksBySpdDmpamId(c.getId()).forEach(e -> {
					SubProjectDocumentTasksItem spdti = new SubProjectDocumentTasksItem();
					spdti.setAssignee(new UserLookupItem(e.getAssignee().getId(), e.getAssignee().getFullName()));
					spdti.setAssigneedBy(new UserLookupItem(e.getAssigneedBy().getId(), e.getAssigneedBy().getFullName()));
					spdti.setComments(e.getComments());
					spdti.setSubStatus(e.getSubStatus());
					spdti.setStatus(e.getStatus());
					spdti.setCompletedOn(e.getCompletedOn());
					spdti.setStartDate(e.getStartDate());
					spdti.setEndDate(e.getEndDate());
					spdti.setId(e.getId());
					spdti.setSubProjectDmPamRef(e.getSubProjectDocumentDmPamTasksRef().getId());
					spddmpamli.addTask(spdti);
				});

				String generalCommentsJSON = c.getGeneralComments();
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

//						gci.setSectionsWithIds(Stream.of(new KeyValue(gc.getSections().getId(), gc.getSections().getName())).collect(Collectors.toSet()));

						spddmpamli.addComment(gci);
					});
				}

				dto.addTask(spddmpamli);
			});
		}
		return dto;
	}
	
	@Transactional
	public void submitSubProjectDocumentSection(UUID docId, UUID userId, SubProjectDocumentSectionRequest body) {
		SubProjectDocument doc = subProjectRepo.findById(docId)
				.orElseThrow(() -> new ValidationException("Invalid ID"));
		
		SubProjectDocumentSection section = doc.getSections().stream()
				.filter(s -> s.getId().equals(body.getId()))
				.findFirst()
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		section.setData(body.getData());
		section.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
	}
	
	
	@Transactional
	public void requestSubProjectDocumentSectionReview(UUID sectionId) {
		SubProjectDocumentSection section = subProjectSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		section.setReviewStatus(ProcessStatus.PENDING.getPersistenceValue());
		section.getSubProjectDocumentRef().setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
	}

	@Transactional
	public void addSubProjectDmPamTasks(AuthPrincipal user, UUID requestId, AddQprTasksRequest body) {
		SubProjectDocument spd = subProjectRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid Sub Process Doc ID"));
		body.getUsersId().forEach(c -> {
			SubProjectDocumentDmPamTasks spddmt = new SubProjectDocumentDmPamTasks();
			spddmt.setAssignee(userRepository.findById(c).get());
			spddmt.setAssigneedBy(userRepository.findById(user.getUserId()).get());
			spddmt.setSubProjectRef(spd);
			spddmt.setComments(body.getComments());
			spddmt.setStartDate(body.getStartDate());
			spddmt.setEndDate(body.getEndDate());
			spddmt.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			spd.addTask(spddmt);
		});
	}

	@Transactional
	public void assignUserReviewsByDmpam(AuthPrincipal user, UUID requestId, AddQprTasksRequest body) {
		SubProjectDocumentDmPamTasks spddmpt = subProjectDmpamTasksRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid DM PAM TASK ID"));
		body.getUsersId().forEach(c -> {
			SubProjectDocumentTasks spdt = new SubProjectDocumentTasks();
			spdt.setAssignee(userRepository.findById(c).get());
			spdt.setAssigneedBy(userRepository.findById(user.getUserId()).get());
			spdt.setSubProjectDocumentDmPamTasksRef(spddmpt);
			spdt.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			spdt.setComments(body.getComments());
			spdt.setStartDate(body.getStartDate());
			spdt.setEndDate(body.getEndDate());
			spdt.setComments(body.getComments());
			spddmpt.addTask(spdt);
		});
		spddmpt.setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
	}

	@Transactional
	public List<SubProjectDocumentDmPamTasksListItem> getSubProjectDmPamTasks(AuthPrincipal user) {
		List<SubProjectDocumentDmPamTasks> spddmpamt = spdDmPamRepo.getSubProjectDocumentDmPamTasksByUserId(user.getUserId());

		List<SubProjectDocumentDmPamTasksListItem> dto = new ArrayList<>();

		spddmpamt.forEach(c -> {
			SubProjectDocumentDmPamTasksListItem spddmpamli = new SubProjectDocumentDmPamTasksListItem();
			spddmpamli.setAssignee(new UserLookupItem(c.getAssignee().getId(), c.getAssignee().getFullName()));
			spddmpamli.setAssigneedBy(new UserLookupItem(c.getAssigneedBy().getId(), c.getAssigneedBy().getFullName()));
			spddmpamli.setSubProjectRef(c.getSubProjectRef().getId());
			spddmpamli.setComments(c.getComments());
			spddmpamli.setStartDate(c.getStartDate());
			spddmpamli.setEndDate(c.getEndDate());
			spddmpamli.setSubStatus(c.getSubStatus());
			spddmpamli.setStatus(c.getStatus());
			spddmpamli.setDocName(c.getSubProjectRef().getDocName());
			spddmpamli.setDocNumber(c.getSubProjectRef().getDocNumber());
			dto.add(spddmpamli);
		});

		return dto;
	}

	@Transactional
	public List<SubProjectDocumentTasksListItem> getSubProjectTasks(AuthPrincipal user) {
		List<SubProjectDocumentTasks> spdt = subProjectTasksRepo.getSubProjectDocumentTasksByUserId(user.getUserId());
		List<SubProjectDocumentTasksListItem> dto = new ArrayList<>();

		spdt.forEach(c -> {
			SubProjectDocumentTasksListItem spdtli = new SubProjectDocumentTasksListItem();
			spdtli.setAssignee(new UserLookupItem(c.getAssignee().getId(), c.getAssignee().getFullName()));
			spdtli.setAssigneedBy(new UserLookupItem(c.getAssigneedBy().getId(), c.getAssigneedBy().getFullName()));
			spdtli.setSubProjectDmpamTaskRef(c.getSubProjectDocumentDmPamTasksRef().getId());
			spdtli.setComments(c.getComments());
			spdtli.setStartDate(c.getStartDate());
			spdtli.setEndDate(c.getEndDate());
			spdtli.setSubStatus(c.getSubStatus());
			spdtli.setStatus(c.getStatus());
			spdtli.setId(c.getId());
			spdtli.setDocName(c.getSubProjectDocumentDmPamTasksRef().getSubProjectRef().getDocName());
			spdtli.setDocNumber(c.getSubProjectDocumentDmPamTasksRef().getSubProjectRef().getDocNumber());
			spdtli.setSubProjectRef(c.getSubProjectDocumentDmPamTasksRef().getSubProjectRef().getId());

			dto.add(spdtli);
		});

		return dto;
	}

	@Transactional
	public void submitSubProjectDocumentSectionReview(UUID sectionId, AddSubProjectSectionReviewRequest body) {
		SubProjectDocumentSection section = subProjectSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));

		section.setComments(body.getComments());

		section.setReviewStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		section.setReviewCompletedOn(new Date());

		boolean allSectionsCompleted = section.getSubProjectDocumentRef().getSections().stream()
				.allMatch(s -> s.getReviewStatus().equals(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue()));

//		System.out.println(section.getSubProjectDocumentRef().getSections().size());
//		System.out.println(allSectionsCompleted);

		if (allSectionsCompleted) {
			section.getSubProjectDocumentRef().setStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
			// TODO Also update Proposal Status
		}

		//TODO: update status of doc
	}

	@Transactional
	public void reassignSubProjectDocumentToFIP(UUID proposalId, UUID userId, Set<UUID> sectionIds, String comments) {
		SubProjectDocument p = subProjectRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid SUB PROCESS DOC ID"));

		sectionIds.forEach(sId -> {
			SubProjectDocumentSection section = p.getSections()
					.stream()
					.filter(s -> s.getId().equals(sId))
					.findFirst()
					.orElseThrow(() -> new ValidationException("Invalid Section ID"));

			section.setReassignmentStatus(ProcessStatus.PENDING.getPersistenceValue());
			section.setReassignmentComments(comments);
		});
	}

	@Transactional
	public void changeSubProjectDocStatus(
			UUID requestId,
			AuthPrincipal principal,
			ProcessStatus status
	){
		SubProjectDocument spd = subProjectRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid SUB PROCESS DOC ID"));
		spd.setStatus(status.getPersistenceValue());
	}

	@Transactional
	public void changeSubProjectDocDmPamTaskStatus(
			UUID requestId,
			AuthPrincipal principal,
			ProcessStatus status
	){
		SubProjectDocumentDmPamTasks spddmpt = subProjectDmpamTasksRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid DM PAM TASK ID"));
		spddmpt.setStatus(status.getPersistenceValue());
	}
}
