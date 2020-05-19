package com.ndrmf.engine.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.CommenceProjectProposalRequest;
import com.ndrmf.engine.dto.ProjectProposalItem;
import com.ndrmf.engine.dto.SectionItem;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.ProjectProposalSection;
import com.ndrmf.engine.model.ProjectProposalSectionReview;
import com.ndrmf.engine.model.ProjectProposalTask;
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
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

@Service
public class ProjectProposalService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ThematicAreaRepository thematicAreaRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private ProjectProposalTaskRepository ptaskRepo;
	
	public UUID commenceProjectProposal(UUID initiatorUserId, CommenceProjectProposalRequest body) {
		ProjectProposal p = new ProjectProposal();
		
		p.setName(body.getName());
		p.setInitiatedBy(userRepo.getOne(initiatorUserId));
		p.setProcessOwner(this.getProcessOwnerForThematicArea(body.getThematicAreaId()));
		p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		
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
}
