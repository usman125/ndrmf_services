package com.ndrmf.engine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ndrmf.engine.model.*;
import com.ndrmf.engine.repository.*;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.AddProposalGeneralCommentRequest;
import com.ndrmf.engine.dto.AddProposalSectionReviewRequest;
import com.ndrmf.engine.dto.AddQualificationSectionReviewRequest;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.KeyValue;
import com.ndrmf.util.enums.ReviewStatus;
import com.ndrmf.util.enums.TaskStatus;

@Service
public class CommentService {
	@Autowired private QualificationSectionRepository qualSectionRepo;
	@Autowired private QualificationTaskRepository qtaskRepo;
	@Autowired private ProjectProposalSectionRepository projPropSectionRepo;
	@Autowired private SubProjectDocumentSectionRepository subProjDocumentSectionRepo;
	@Autowired private SubProjectDocumentDmPamTasksRepository subProjDocumentDmpamTasksRepo;
	@Autowired private SubProjectDocumentTasksRepository subProjDocumentTasksRepo;
	@Autowired private ProjectProposalTaskRepository projPropTaskRepo;
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private UserRepository userRepo;
	@Autowired private NotificationService notificationService;
	
	@Transactional
	public void addQualificationSectionReview(UUID byUserId, UUID sectionId, AddQualificationSectionReviewRequest body) {
		QualificationSection section = qualSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		if(!section.getSme().getId().equals(byUserId)) {
			throw new ValidationException("Only SME can add review for this section. Authorized SME is: " + section.getSme().getFullName());
		}
		
		QualificationSectionReview qsr = new QualificationSectionReview();
		qsr.setComments(body.getComments());
		qsr.setControlWiseComments(body.getControlWiseComments());
		qsr.setRating(body.getRating());
		qsr.setStatus(body.getStatus());
		
		section.setStatus(body.getStatus());
		section.setReviewStatus(ReviewStatus.COMPLETED.getPersistenceValue());
		
		section.addReview(qsr);
		
		List<QualificationTask> tasks = qtaskRepo.findTasksForSectionAndAssignee(byUserId, sectionId);
		
		tasks.forEach(t -> {
			t.setStatus(TaskStatus.COMPLETED.getPersistenceValue());
		});

		try {
			notificationService.sendPlainTextEmail(
					section.getQualifcationRef().getProcessOwner().getEmail(),
					section.getQualifcationRef().getProcessOwner().getFullName(),
					"Review submitted on qualification request",
					section.getSme().getFullName() +
					", has submitted their remarks on qualification request.\n" +
					"Please visit http://ndrmfdev.herokuapp.com/all-qualification-requests/"+section.getQualifcationRef().getId()
					+ " to review the remarks. \n" +
					"Comments from SME: " + body.getComments()
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void addProjectProposalSectionReview(UUID byUserId,
												UUID sectionId,
												AddProposalSectionReviewRequest body) {
		ProjectProposalSection section = projPropSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		if(!section.getSme().getId().equals(byUserId)) {
			throw new ValidationException("Only SME can add review for this section. Authorized SME is: " + section.getSme().getFullName());
		}
		
		ProjectProposalSectionReview qsr = new ProjectProposalSectionReview();
		qsr.setComments(body.getComments());
		qsr.setReviewAddedBy(userRepo.getOne(byUserId));
		
		section.setStatus(body.getStatus());
		section.setReviewStatus(ReviewStatus.COMPLETED.getPersistenceValue());
		section.setReviewCompletedOn(new Date());
		
		section.addReview(qsr);
		
		List<ProjectProposalTask> tasks = projPropTaskRepo.findTasksForSectionAndAssignee(byUserId, sectionId);
		
		tasks.forEach(t -> {
			t.setStatus(TaskStatus.COMPLETED.getPersistenceValue());
		});
	}
	
	@Transactional
	public void addProjectProposalGeneralComment(UUID proposalId,
												 AuthPrincipal byUser,
												 AddProposalGeneralCommentRequest body) {
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		String generalCommentsJSON = p.getGeneralComments();
		List<ProjectProposalGeneralCommentModel> generalComments = new ArrayList<ProjectProposalGeneralCommentModel>();
		
		if(generalCommentsJSON != null) {
			try {
				generalComments = objectMapper.readValue(generalCommentsJSON, objectMapper.getTypeFactory().constructCollectionType(List.class, ProjectProposalGeneralCommentModel.class));
			} catch (Exception e) {
				throw new RuntimeException("General Comments are not null but couldn't read it as JSON", e);
			}
		}
		
		ProjectProposalGeneralCommentModel newComment = new ProjectProposalGeneralCommentModel();
		
		List<KeyValue> sections = new ArrayList<>();
		
		if(body.getSectionIds() != null) {
			body.getSectionIds().forEach(sid -> {
				ProjectProposalSection pps = p.getSections().stream()
						.filter(ps -> ps.getId().equals(sid))
						.findAny()
						.orElseThrow(() -> new ValidationException("Invalid Section ID"));
				
				sections.add(new KeyValue(pps.getId(), pps.getName()));
				
			});	
		}
		else {
			sections.add(new KeyValue(null, "General"));
		}
		
		newComment.setSections(sections);
		newComment.setCreatedAt(new Date());
		newComment.setAddedBy(new KeyValue(byUser.getUserId(), byUser.getFullName()));
		newComment.setComment(body.getComment());
		newComment.setStage(body.getStage());
		
		generalComments.add(newComment);
		
		try {
			p.setGeneralComments(objectMapper.writeValueAsString(generalComments));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Couldn't write comment as JSON", e);
		}
	}

	@Transactional
	public void addSubProjectDocumentGeneralComment(UUID requestId,
													UUID taskId,
												 AuthPrincipal byUser,
												 AddProposalGeneralCommentRequest body) {
		SubProjectDocumentDmPamTasks p = subProjDocumentDmpamTasksRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		SubProjectDocumentTasks spdt = subProjDocumentTasksRepo.findById(taskId)
				.orElseThrow(() -> new ValidationException("Invalid task ID"));

		String generalCommentsJSON = p.getGeneralComments();
		List<ProjectProposalGeneralCommentModel> generalComments = new ArrayList<ProjectProposalGeneralCommentModel>();

		if(generalCommentsJSON != null) {
			try {
				generalComments = objectMapper.readValue(generalCommentsJSON, objectMapper.getTypeFactory().constructCollectionType(List.class, ProjectProposalGeneralCommentModel.class));
			} catch (Exception e) {
				throw new RuntimeException("General Comments are not null but couldn't read it as JSON", e);
			}
		}

		ProjectProposalGeneralCommentModel newComment = new ProjectProposalGeneralCommentModel();

		List<KeyValue> sections = new ArrayList<>();

		if(body.getSectionIds() != null) {
			body.getSectionIds().forEach(sid -> {
				SubProjectDocumentSection pps = p.getSubProjectRef().getSections().stream()
						.filter(ps -> ps.getId().equals(sid))
						.findAny()
						.orElseThrow(() -> new ValidationException("Invalid Section ID"));

				sections.add(new KeyValue(pps.getSectionRef().getId(), pps.getSectionRef().getName()));

			});
		}
		else {
			sections.add(new KeyValue(null, "General"));
		}

		newComment.setSections(sections);
		newComment.setCreatedAt(new Date());
		newComment.setAddedBy(new KeyValue(byUser.getUserId(), byUser.getFullName()));
		newComment.setComment(body.getComment());
		newComment.setStage(body.getStage());

		generalComments.add(newComment);

		try {
			p.setGeneralComments(objectMapper.writeValueAsString(generalComments));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Couldn't write comment as JSON", e);
		}

		spdt.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		spdt.setCompletedOn(new Date());

		if (p.getTasks().stream().allMatch(r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()))){
			p.setStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		}

		try {
			notificationService.sendPlainTextEmail(
				p.getAssignee().getEmail(),
				p.getAssignee().getFullName(),
				"Sub Project Document Scheme task assigned on Project Proposal " + p.getSubProjectRef().getProposalRef().getName() + " at NDRMF",
				spdt.getAssignee().getFullName()
				+  " has submitted their remarks on Sub Project Document Scheme for Project Proposal "
				+ p.getSubProjectRef().getProposalRef().getName()
				+ "\nPlease visit http://ndrmfdev.herokuapp.com/view-sub-project-document/"+p.getSubProjectRef().getId()
				+ " to review and process the request(s).\n"
				+ "Sub Project Document Number: " + p.getSubProjectRef().getDocNumber()
				+ "\nSub Project Document Name: " + p.getSubProjectRef().getDocName()
				+ "\nComments from others: : " + body.getComment()
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}