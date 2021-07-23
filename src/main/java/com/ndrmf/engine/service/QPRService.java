package com.ndrmf.engine.service;

import java.time.LocalDate;
import java.util.*;

import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.model.*;
import com.ndrmf.engine.repository.*;
import com.ndrmf.user.dto.QprUserLookUpItem;
import com.ndrmf.user.model.Role;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.qpr.QPRSectionRequest;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportItem;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportListItem;
import com.ndrmf.engine.repository.qpr.QPRTaskRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.constants.SystemRoles;

import javax.transaction.Transactional;

@Service
public class QPRService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private QuarterlyProgressReportRepository qprRepo;
	@Autowired private QPRTaskRepository qprTasksRepo;
	@Autowired private UserRepository userRepository;
	@Autowired private QuarterlyProgressReportSectionRepository quarterlyProgressReportSectionRepository;
	@Autowired private QuarterlyProgressReportTaskRepository quarterlyProgressReportTaskRepository;
	@Autowired private QuarterlyProgressReportTaskReviewRepository qprTaskReviewRepo;
	
	public UUID commenceQPR(UUID proposalId, DateAndCommentBody body) {
		com.ndrmf.setting.model.ProcessType  processType = processTypeRepo.findById(ProcessType.QPR.toString())
				.orElseThrow(() -> new RuntimeException("QPR Process Type is undefined in the system."));
		
		if(processType.getOwner() == null) {
			new RuntimeException("Process owner undefined for QPR Process");
		}

		ProjectProposal p;
		p = projProposalRepo.getOne(proposalId);

		List<QuarterlyProgressReport> prevQprs;
		prevQprs = qprRepo.getQuarterlyProgressReportsByProposalId(proposalId);

		QuarterlyProgressReport qpr = new QuarterlyProgressReport();

		if (prevQprs != null && prevQprs.size() > 0){
			qpr.setQuarter(prevQprs.get(prevQprs.size() - 1).getQuarter() + 1);
//			qpr.setDueDate(prevQprs.get(prevQprs.size() - 1).getDueDate().plusMonths(3));
			qpr.setDueDate(body.getDueDate());
			qpr.setAssignedComments(body.getComments());
		}else{
			qpr.setQuarter(1);
//			qpr.setDueDate(LocalDate.now().withMonth(3).withDayOfMonth(31).plusDays(10));
			qpr.setDueDate(body.getDueDate());
			qpr.setAssignedComments(body.getComments());
		}

		qpr.setProposalRef(p);
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
		
//		if(principal.getRoles().contains(SystemRoles.ORG_FIP)
//		|| principal.getRoles().contains(SystemRoles.PROCESS_OWNER)) {
			qprs = qprRepo.getQuarterlyProgressReportsForFIP(principal.getUserId());
//		}
		
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
		dto.setProposalRef(qpr.getProposalRef().getId());
		dto.setImplementationPlan(qpr.getProposalRef().getPip().getImplementationPlan());

		dto.setAssignedComments(qpr.getAssignedComments());
		
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

//		List<QuarterlyProgressReportTask> qprtl = quarterlyProgressReportTaskRepository.findTasksForQprWithNoSection(qpr.getId());
				List<QuarterlyProgressReportTask> qprtl = qprTasksRepo.findTasksForQprWithNoSection(qpr.getId());

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
			section.setQprId(qpr.getId());
			List<QuarterlyProgressReportTask> tasks = qprTasksRepo.findTasksForSection(qs.getId(), PageRequest.of(0, 1));

			if (tasks != null && tasks.size() > 0) {
				section.setReviewDeadline(tasks.get(0).getEndDate());
			}
			
			if (qs.getReviews() != null && qs.getReviews().size() > 0) {
				QuarterlyProgressReportSectionReview latestReview;
				latestReview = qs.getReviews().get(qs.getReviews().size() - 1);
				section.setReview(
						latestReview.getCreatedDate(),
						null,
						null,
						latestReview.getStatus(),
						latestReview.getComments()
				);
				qs.getReviews().forEach(r -> {
					section.addReviewHistory(
							r.getCreatedDate(),
							null,
							null,
							r.getStatus(), r.getComments()
					);
				});
			}
			dto.addSection(section);
		});

		qprtl.forEach(t -> {
			TaskItem.TaskItemForQpr ti = new TaskItem.TaskItemForQpr();

			ti.setComments(t.getComments());
			ti.setEndDate(t.getEndDate());
			ti.setRequestId(t.getQpr().getId());
			ti.setStartDate(t.getStartDate());
			ti.setTaskId(t.getId());
			if (ti.getSectionId() != null){
				ti.setSectionId(t.getSection().getId());
			}
			if (ti.getSectionId() != null){
				ti.setSectionName(t.getSection().getName());
			}
			ti.setStatus(t.getStatus());
			if(t.getQpr().getProposalRef() != null) {
				ti.setFipName(t.getQpr().getProposalRef().getInitiatedBy().getFullName());
			}

			List<String> roles = new ArrayList<>();

			if(t.getAssignee().getRoles() != null) {
				t.getAssignee().getRoles().forEach(r -> {
//					Map<String, Object> role = new HashMap<>();
//					role.put("id", r.getId());
//					role.put("name", r.getName());

					roles.add(r.getName());
				});

			}


			ti.setAssignee(new QprUserLookUpItem(
					t.getAssignee().getId(),
					t.getAssignee().getFullName(),
					roles,
					t.getAssignee().getDepartment().getName()
				)
			);

			Optional<QuarterlyProgressReportTaskReview> qprtr = qprTaskReviewRepo.findReviewsForTask(t.getId());

			if (qprtr.isPresent()){
				ti.setOthersDecision(qprtr.get().getDecision());
				ti.setOthersRemarks(qprtr.get().getComments());
				ti.setReviewCompletedOn(qprtr.get().getReviewCompletedOn());
			}

			dto.addTasksForOthers(ti);
		});


		System.out.println(qprtl.size());

		return dto;
	}
	
	public void submitQPRSection(
			UUID id, UUID userId,
		 	QPRSectionRequest body,
			FormAction action) {
		
		QuarterlyProgressReport qpr = qprRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid QPR ID"));
		
		if (!qpr.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())
				&& !qpr.getStatus().equals(ProcessStatus.REASSIGNED.getPersistenceValue())) {
			throw new ValidationException("Request is already: " + qpr.getStatus());
		}
		
		QuarterlyProgressReportSection section = qpr.getSections().stream().filter(s -> s.getId().equals(body.getId())).findAny()
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));

		section.setData(body.getData());

		if (action == FormAction.SUBMIT) {
			qpr.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		}

		if (section.getReassignmentStatus() != null
				&& section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
			section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
		}

		qprRepo.save(qpr);

		if (action == FormAction.SUBMIT) {
			// TODO raise event
			// eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));
		}
	}


	@Transactional
	public void addQprSectionTask(UUID sectionId, UUID currentUserId, AddQualificationTaskRequest body) {
		QuarterlyProgressReportSection section;
		section = quarterlyProgressReportSectionRepository.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		if(!section.getQprRef().getProcessOwner().getId().equals(currentUserId)) {
			throw new ValidationException("Only Process Owner for this process can add tasks. Authorized user is: "+ section.getQprRef().getProcessOwner().getFullName());
		}
		QuarterlyProgressReportTask task = new QuarterlyProgressReportTask();
		task.setStartDate(body.getStartDate());
		task.setEndDate(body.getEndDate());
		task.setComments(body.getComments());
		task.setSection(section);
		task.setAssignee(section.getSme());
		task.setStatus(TaskStatus.PENDING.getPersistenceValue());
		task.setQpr(section.getQprRef());
		section.setReviewStatus(ReviewStatus.PENDING.getPersistenceValue());
		section.getQprRef().setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
		quarterlyProgressReportTaskRepository.save(task);

	}

	@Transactional
	public void addTasksForQpr(UUID qprId, UUID currentUserId, AddQprTasksRequest body) {
		QuarterlyProgressReport qpr;
		qpr = qprRepo.findById(qprId)
				.orElseThrow(() -> new ValidationException("Invalid QPR ID"));
		if(!qpr.getProcessOwner().getId().equals(currentUserId)) {
			throw new ValidationException("Only Process Owner for this process can add tasks. Authorized user is: "+ qpr.getProcessOwner().getFullName());
		}
		body.getUsersId().forEach(userId -> {

			Optional<QuarterlyProgressReportTask> oqprt = qprTasksRepo.findTasksForUserWithNoSection(userId);

			if (oqprt.isPresent()){
				oqprt.get().setStatus(ProcessStatus.PENDING.getPersistenceValue());
				oqprt.get().setStartDate(body.getStartDate());
				oqprt.get().setEndDate(body.getEndDate());
				oqprt.get().setComments(body.getComments());
			}else{
				QuarterlyProgressReportTask task = new QuarterlyProgressReportTask();
				task.setStartDate(body.getStartDate());
				task.setEndDate(body.getEndDate());
				task.setComments(body.getComments());
				task.setAssignee(userRepository.findById(userId).get());
				task.setStatus(TaskStatus.PENDING.getPersistenceValue());
				task.setQpr(qpr);

				quarterlyProgressReportTaskRepository.save(task);
			}
		});

	}

	@Transactional
	public void addQprSectionReview(UUID byUserId, UUID sectionId, AddQprSectionReviewRequest body) {
		QuarterlyProgressReportSection section = quarterlyProgressReportSectionRepository.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));

		if(!section.getSme().getId().equals(byUserId)) {
			throw new ValidationException("Only SME can add review for this section. Authorized SME is: " + section.getSme().getFullName());
		}

		QuarterlyProgressReportSectionReview qprsr = new QuarterlyProgressReportSectionReview();
		qprsr.setComments(body.getComments());
		qprsr.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());

		section.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		section.setReviewStatus(ReviewStatus.COMPLETED.getPersistenceValue());
		section.setReviewCompletedOn(new Date());
		section.addReview(qprsr);

		List<QuarterlyProgressReportTask> tasks = quarterlyProgressReportTaskRepository.findTasksForSectionAndAssignee(byUserId, sectionId);


		tasks.forEach(t -> {
			t.setStatus(TaskStatus.COMPLETED.getPersistenceValue());
		});

		List<QuarterlyProgressReportTask> peningTasks = quarterlyProgressReportTaskRepository.findTasksForQpr(section.getQprRef().getId());

		System.out.println(peningTasks);
		System.out.println(peningTasks.size());

		if (peningTasks.stream().anyMatch(r -> r.getStatus() == ProcessStatus.PENDING.getPersistenceValue())){
			section.getQprRef().setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
		}else{
			section.getQprRef().setStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		}
	}

	@Transactional
	public void addQprReviewByDepUser(UUID byUserId, UUID taskId, AddQprTaskReviewRequest body) {
		QuarterlyProgressReportTask qprt = qprTasksRepo.findById(taskId)
				.orElseThrow(() -> new ValidationException("Invalid TASK ID"));

		Optional<QuarterlyProgressReportTaskReview> oqprtr = qprTaskReviewRepo.findReviewsForTask(qprt.getId());

		if (oqprtr.isPresent()){
			oqprtr.get().setDecision(body.getDecision());
			oqprtr.get().setComments(body.getComments());
			oqprtr.get().setReviewCompletedOn(new Date());
		}else{
			QuarterlyProgressReportTaskReview qprtr = new QuarterlyProgressReportTaskReview();
			qprtr.setDecision(body.getDecision());
			qprtr.setComments(body.getComments());
			qprtr.setQprRef(qprt.getQpr());
			qprtr.setQprTaskRef(qprt);
			qprtr.setReviewCompletedOn(new Date());
			qprTaskReviewRepo.save(qprtr);
		}
		qprt.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
	}

	@Transactional
	public void extendQprTimeline(UUID byUserId, UUID qprId, DateAndCommentBody body) {
		QuarterlyProgressReport qpr = qprRepo.findById(qprId)
				.orElseThrow(() -> new ValidationException("Invalid QPR ID"));


		qpr.setDueDate(body.getDueDate());
		qpr.setAssignedComments(body.getComments());
//		System.out.println(qpr.getDueDate());
//		System.out.println(body.getDueDate());
//		System.out.println(qpr.getAssignedComments() + body.getComments());
	}
}
