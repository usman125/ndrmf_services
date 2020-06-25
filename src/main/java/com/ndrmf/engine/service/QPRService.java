package com.ndrmf.engine.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.SectionItem;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportItem;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportListItem;
import com.ndrmf.engine.model.QuarterlyProgressReport;
import com.ndrmf.engine.model.QuarterlyProgressReportSection;
import com.ndrmf.engine.model.QuarterlyProgressReportSectionReview;
import com.ndrmf.engine.model.QuarterlyProgressReportTask;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.QuarterlyProgressReportRepository;
import com.ndrmf.engine.repository.qpr.QPRTaskRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

@Service
public class QPRService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private QuarterlyProgressReportRepository qprRepo;
	@Autowired private QPRTaskRepository qprTasksRepo;
	
	public UUID commenceQPR(UUID proposalId) {
		com.ndrmf.setting.model.ProcessType  processType = processTypeRepo.findById(ProcessType.QPR.toString())
				.orElseThrow(() -> new RuntimeException("QPR Process Type is undefined in the system."));
		
		if(processType.getOwner() == null) {
			new RuntimeException("Process owner undefined for QPR Process");
		}
		
		QuarterlyProgressReport qpr = new QuarterlyProgressReport();
		qpr.setQuarter(1);
		qpr.setDueDate(LocalDate.now().withMonth(3).withDayOfMonth(31));
		qpr.setProposalRef(projProposalRepo.getOne(proposalId));
		qpr.setProcessOwner(processType.getOwner());
		qpr.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		
		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.QPR.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for QPR process");
		}

		for (SectionTemplate st : sts) {
			QuarterlyProgressReportSection ps = new QuarterlyProgressReportSection();
			ps.setName(st.getSection().getName());
			ps.setTemplateType(st.getTemplateType());
			ps.setTemplate(st.getTemplate());
			ps.setSme(st.getSection().getSme());
			ps.setSectionRef(st.getSection());
			ps.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
			
			qpr.addSection(ps);
		}
		
		return qprRepo.save(qpr).getId();
	}
	
	public List<QuarterlyProgressReportListItem> getQPRRequests(AuthPrincipal principal){
		List<QuarterlyProgressReport> qprs = new ArrayList<>();
		
		if(principal.getRoles().contains(SystemRoles.ORG_FIP)) {
			qprs = qprRepo.getQuarterlyProgressReportsForFIP(principal.getUserId());
		}
		
		List<QuarterlyProgressReportListItem> dtos = new ArrayList<>();
		qprs.forEach(r -> {
			QuarterlyProgressReportListItem item = new QuarterlyProgressReportListItem();
			
			item.setId(r.getId());
			item.setDueDate(r.getDueDate());
			item.setFipName(r.getProposalRef().getInitiatedBy().getFullName());
			item.setProposalName(r.getProposalRef().getName());
			item.setQuarter(r.getQuarter());
			item.setStatus(r.getStatus());
			
			dtos.add(item);
		});
		
		return dtos;
	}
	
	public QuarterlyProgressReportItem getQPRRequest(UUID id, AuthPrincipal principal) {
		QuarterlyProgressReport qpr = qprRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid ID"));
		
		QuarterlyProgressReportItem dto = new QuarterlyProgressReportItem();
		dto.setDueDate(qpr.getDueDate());
		dto.setFipName(qpr.getProposalRef().getInitiatedBy().getFullName());
		dto.setProcessOwnerName(qpr.getProcessOwner().getFullName());
		dto.setQuarter(qpr.getQuarter());
		dto.setStatus(qpr.getStatus());
		dto.setSubmittedAt(qpr.getSubmittedAt());
		
		if(qpr.getProposalRef().getInitiatedBy().getId().equals(principal.getUserId())) {
			List<QuarterlyProgressReportTask> reassignmentComments = 
					qprTasksRepo.findAllTasksForAssigneeAndRequest(principal.getUserId(), id);
			
			if (reassignmentComments != null && reassignmentComments.size() > 0) {
				QuarterlyProgressReportTask lastTask = reassignmentComments.get(reassignmentComments.size() - 1);
				
				TaskItem ti = new TaskItem();
				ti.setComments(lastTask.getComments());
				ti.setEndDate(lastTask.getEndDate());
				ti.setStartDate(lastTask.getStartDate());
				ti.setStatus(lastTask.getStatus());

				dto.setReassignmentTask(ti);
			}
		}
		
		qpr.getSections().forEach(qs -> {
			SectionItem section = new SectionItem();
			section.setAssigned(qs.getSme().getId().equals(principal.getUserId()));
			section.setData(qs.getData());
			section.setId(qs.getId());
			section.setName(qs.getName());
			section.setSme(new UserLookupItem(qs.getSme().getId(), qs.getSme().getFullName()));
			section.setTemplate(qs.getTemplate());
			section.setTemplateType(qs.getTemplateType());
			section.setReviewStatus(qs.getReviewStatus());
			section.setReviewCompletedDate(qs.getReviewCompletedOn());
			section.setReassignmentStatus(qs.getReassignmentStatus());
			
			List<QuarterlyProgressReportTask> tasks = qprTasksRepo.findTasksForSection(qs.getId(), PageRequest.of(0, 1));

			if (tasks != null && tasks.size() > 0) {
				section.setReviewDeadline(tasks.get(0).getEndDate());
			}
			
			if (qs.getReviews() != null && qs.getReviews().size() > 0) {
				QuarterlyProgressReportSectionReview latestReview = qs.getReviews().get(qs.getReviews().size() - 1);

				section.setReview(latestReview.getCreatedDate(), null, null, latestReview.getStatus(),
						latestReview.getComments());

				qs.getReviews().forEach(r -> {
					section.addReviewHistory(r.getCreatedDate(), null, null, r.getStatus(), r.getComments());
				});
			}
			
			dto.addSection(section);
		});
		
		return dto;
	}
}
