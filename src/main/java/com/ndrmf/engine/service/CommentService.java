package com.ndrmf.engine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.AddProposalGeneralCommentRequest;
import com.ndrmf.engine.dto.AddProposalSectionReviewRequest;
import com.ndrmf.engine.dto.AddQualificationSectionReviewRequest;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.ProjectProposalGeneralCommentModel;
import com.ndrmf.engine.model.ProjectProposalSection;
import com.ndrmf.engine.model.ProjectProposalSectionReview;
import com.ndrmf.engine.model.ProjectProposalTask;
import com.ndrmf.engine.model.QualificationSection;
import com.ndrmf.engine.model.QualificationSectionReview;
import com.ndrmf.engine.model.QualificationTask;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.ProjectProposalSectionRepository;
import com.ndrmf.engine.repository.ProjectProposalTaskRepository;
import com.ndrmf.engine.repository.QualificationSectionRepository;
import com.ndrmf.engine.repository.QualificationTaskRepository;
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
	@Autowired private ProjectProposalTaskRepository projPropTaskRepo;
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private UserRepository userRepo;
	
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
	}
	
	@Transactional
	public void addProjectProposalSectionReview(UUID byUserId, UUID sectionId, AddProposalSectionReviewRequest body) {
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
	public void addProjectProposalGeneralComment(UUID proposalId, AuthPrincipal byUser, AddProposalGeneralCommentRequest body) {
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
		
		generalComments.add(newComment);
		
		try {
			p.setGeneralComments(objectMapper.writeValueAsString(generalComments));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Couldn't write comment as JSON", e);
		}
	}
}