package com.ndrmf.engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.CommenceProjectProposalRequest;
import com.ndrmf.engine.dto.PreliminaryAppraisalItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalListItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalRequest;
import com.ndrmf.engine.dto.ProjectProposalItem;
import com.ndrmf.engine.dto.ProjectProposalListItem;
import com.ndrmf.engine.dto.ProjectProposalSectionRequest;
import com.ndrmf.engine.dto.SectionItem;
import com.ndrmf.engine.model.PreliminaryAppraisal;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.ProjectProposalSection;
import com.ndrmf.engine.model.ProjectProposalSectionReview;
import com.ndrmf.engine.model.ProjectProposalTask;
import com.ndrmf.engine.repository.PreliminaryAppraisalRepository;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.ProjectProposalTaskRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.setting.repository.ThematicAreaRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;
import com.ndrmf.util.enums.ReassignmentStatus;

@Service
public class ProjectProposalService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ThematicAreaRepository thematicAreaRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private ProjectProposalTaskRepository ptaskRepo;
	@Autowired private UserService userService;
	@Autowired private PreliminaryAppraisalRepository preAppRepo;
	
	public UUID commenceProjectProposal(UUID initiatorUserId, CommenceProjectProposalRequest body) {
		if(body.getThematicAreaId() == null) {
			throw new ValidationException("Thematic area cannot be null");
		}
		ProjectProposal p = new ProjectProposal();
		
		p.setName(body.getName());
		p.setInitiatedBy(userRepo.getOne(initiatorUserId));
		p.setProcessOwner(this.getProcessOwnerForThematicArea(body.getThematicAreaId()));
		p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		p.setThematicArea(thematicAreaRepo.getOne(body.getThematicAreaId()));
		
		List<SectionTemplate> sts = 
				sectionTemplateRepo.findTemplatesForProcessType(ProcessType.PROJECT_PROPOSAL.toString());
		
		if(sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for this process");
		}
		
		for(SectionTemplate st: sts) {
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
		
		p = projProposalRepo.save(p);
		
		return p.getId();
		
	}
	
	private User getProcessOwnerForThematicArea(UUID id) {
		ThematicArea area = thematicAreaRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid Thematic area ID"));
		
		
		if(area.getProcessOwner() == null) {
			throw new ValidationException("Process Owner is not defined for this thematic area");
		}
		
		return area.getProcessOwner();
	}
	
	public ProjectProposalItem getProjectProposalRequest(UUID id, UUID userId) {
		ProjectProposal p = projProposalRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		
		ProjectProposalItem dto = new ProjectProposalItem();
		
		dto.setInitiatedBy(new UserLookupItem(p.getInitiatedBy().getId(), p.getInitiatedBy().getFullName()));
		dto.setOwner(p.getProcessOwner().getId().equals(userId));
		dto.setProcessOwner(new UserLookupItem(p.getProcessOwner().getId(), p.getProcessOwner().getFullName()));
		dto.setStatus(p.getStatus());
		dto.setSubmittedAt(p.getCreatedDate());
		
		if(p.getInitiatedBy().getId().equals(userId)) {
			List<ProjectProposalTask> reassignmentComments = 
					ptaskRepo.findAllTasksForAssigneeAndRequest(userId, id);
			
			if(reassignmentComments != null && reassignmentComments.size() > 0) {
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
				ProjectProposalSectionReview latestReview = 
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
	
	public List<ProjectProposalListItem> getProjectProposalRequests(UUID userId, ProcessStatus status){
		List<ProjectProposal> props;
		
		if(status == null) {
			props = projProposalRepo.findAllRequestsForOwnerOrInitiator(userId);
		}
		else {
			props = projProposalRepo.findRequestsForOwnerOrInitiatorByStatus(userId, status.getPersistenceValue());
		}
		
		List<ProjectProposalListItem> dtos = props.stream()
				.map(q -> new ProjectProposalListItem(q.getId(), q.getName(), q.getThematicArea().getName(), q.getInitiatedBy().getFullName(), q.getCreatedDate(), q.getStatus()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	public void submitSection(UUID userId, UUID requestId, ProjectProposalSectionRequest body, FormAction action) {
		ProjectProposal p = projProposalRepo.findById(requestId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		if(!p.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())
				&& !p.getStatus().equals(ProcessStatus.REASSIGNED.getPersistenceValue())) {
			throw new ValidationException("Request is already: " + p.getStatus());
		}
		
		ProjectProposalSection section =
				p.getSections().stream().filter(s -> s.getId().equals(body.getId()))
				.findAny()
				.orElseThrow(() -> new ValidationException("Invalid ID"));
		
		section.setData(body.getData());
		
		if(action == FormAction.SAVE) {
			p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());	
		}
		else if(action == FormAction.SUBMIT) {
			p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		}
		
		if(section.getReassignmentStatus() != null && section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
			section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
		}
		
		projProposalRepo.save(p);
		
		if(action == FormAction.SUBMIT) {
			//TODO raise event
			//eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));	
		}
	}
	
	@Transactional
	public void proposalSubmittedPostProcessor(ProjectProposal proposal) {
		List<SectionTemplate> sts = 
				sectionTemplateRepo.findTemplatesForProcessType(ProcessType.PRELIMINARY_APPRAISAL.toString());
		
		if(sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for PRELIMINARY_APPRAISAL process");
		}
		
		PreliminaryAppraisal appraisal = new PreliminaryAppraisal();
		
		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));
		
		appraisal.setAssignee(dmPAM);
		appraisal.setPassingScore(sts.get(0).getPassingScore());
		appraisal.setTotalScore(sts.get(0).getTotalScore());
		appraisal.setTemplateType(sts.get(0).getTemplateType());
		appraisal.setTemplate(sts.get(0).getTemplate());
		appraisal.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		
		proposal.setPreAppraisal(appraisal);
	}
	
	@Transactional
	public void addPreliminaryAppraisal(UUID userId, UUID proposalId, PreliminaryAppraisalRequest body) {
		ProjectProposal proposal = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
		
		PreliminaryAppraisal appraisal = proposal.getPreAppraisal();
		
		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("No DM PAM is defined in the system"));
		
		if(!dmPAM.getId().equals(userId)) {
			throw new ValidationException("Only DM PAM can add Pre-Appriasal. Authorized User is: "+dmPAM.getFullName());
		}
		
		appraisal.setData(body.getData());
		appraisal.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		
		proposal.setPreAppraisal(appraisal);
	}
	
	public List<PreliminaryAppraisalListItem> getAllPreliminaryAppraisals(UUID userId, ProcessStatus status) {
		List<PreliminaryAppraisal> preApps;
		
		if(status != null) {
			preApps = preAppRepo.findAllAppraisalsForAssigneeAndStatus(userId, status.getPersistenceValue());
		}
		else {
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
}
