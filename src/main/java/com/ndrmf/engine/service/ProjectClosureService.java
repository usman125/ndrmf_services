package com.ndrmf.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.ProjectClosureTaskSubmitRequest;
import com.ndrmf.engine.dto.CommenceProjectClosure;
import com.ndrmf.engine.dto.ProjectClosureTasksListItem;

import com.ndrmf.engine.model.ProjectClosure;
import com.ndrmf.engine.model.ProjectClosureTasks;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.repository.ProjectClosureRepository;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.ProjectClosureTasksRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.util.ArrayList;


@Service
public class ProjectClosureService {
	@Autowired
	private SectionTemplateRepository sectionTemplateRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectClosureRepository projClosureRepo;
	@Autowired
	private ProjectClosureTasksRepository projClosureTasksRepo;
	@Autowired
	private ProjectProposalRepository projPropRepo;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProcessTypeRepository processTypeRepo;
	@Autowired
	private NotificationService notificationService;


	public void commenceProjectClosure(AuthPrincipal initiatorPrincipal, CommenceProjectClosure body) {

		ProjectProposal p = projPropRepo.findById(body.getProposalId())
				.orElseThrow(() -> new ValidationException("INALID PROPOSAL ID"));

		ProjectClosure pc = new ProjectClosure();

		pc.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		pc.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		pc.setGeneralComments(body.getComments());
		pc.setCreated_at(new Date());
		pc.setProjectProposalRef(p);

		User qprPo = null;
		User disbursmentPo = null;
		User giaPo = null;
		User proposalPo = null;

		com.ndrmf.setting.model.ProcessType qprProcessType = processTypeRepo
				.findById(ProcessStatus.QPR.getPersistenceValue())
				.orElseThrow(() -> new ValidationException("QPR Process is not defined."));

		com.ndrmf.setting.model.ProcessType giaProcessType = processTypeRepo
				.findById(ProcessStatus.GIA.getPersistenceValue())
				.orElseThrow(() -> new ValidationException("GIA Process is not defined."));

		com.ndrmf.setting.model.ProcessType disbursmentProcessType = processTypeRepo
				.findById(com.ndrmf.util.enums.ProcessType.DISBURSEMENT.name())
				.orElseThrow(() -> new ValidationException("Disbursment Process is not defined."));

		com.ndrmf.setting.model.ProcessType proposalProcessType = processTypeRepo
				.findById(com.ndrmf.util.enums.ProcessType.PROJECT_PROPOSAL.name())
				.orElseThrow(() -> new ValidationException("PROJECT PROPOSAL Process is not defined."));

		if (qprProcessType.getOwner() != null) {
			qprPo = qprProcessType.getOwner();
			ProjectClosureTasks pct = new ProjectClosureTasks();
			pct.setProjectClosureRef(pc);
			pct.setAssignee(qprPo);
			pct.setInitiatedBy(pc.getInitiatedBy());
			pct.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			pct.setOrderNum(1);
			pct.setGeneralComments(body.getComments());

			pc.addTask(pct);
		}

		if (giaProcessType.getOwner() != null) {
			giaPo = giaProcessType.getOwner();
			ProjectClosureTasks pct = new ProjectClosureTasks();
			pct.setProjectClosureRef(pc);
			pct.setAssignee(giaPo);
			pct.setInitiatedBy(pc.getInitiatedBy());
			pct.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			pct.setOrderNum(2);
			pct.setGeneralComments(body.getComments());

			pc.addTask(pct);
		}

		if (disbursmentProcessType.getOwner() != null) {
			disbursmentPo = disbursmentProcessType.getOwner();
			ProjectClosureTasks pct = new ProjectClosureTasks();
			pct.setProjectClosureRef(pc);
			pct.setAssignee(disbursmentPo);
			pct.setInitiatedBy(pc.getInitiatedBy());
			pct.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			pct.setOrderNum(3);
			pct.setGeneralComments(body.getComments());

			pc.addTask(pct);
		}
		if (proposalProcessType.getOwner() != null) {
			proposalPo = proposalProcessType.getOwner();
			ProjectClosureTasks pct = new ProjectClosureTasks();
			pct.setProjectClosureRef(pc);
			pct.setAssignee(proposalPo);
			pct.setInitiatedBy(pc.getInitiatedBy());
			pct.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			pct.setOrderNum(4);
			pct.setGeneralComments(body.getComments());

			pc.addTask(pct);
		}

		p.setPc(pc);
//		projClosureRepo.save(pc);
	}


	@Transactional
	public List<ProjectClosureTasksListItem> getProjectClosureRequestsByProposalId(AuthPrincipal currentUser, UUID proposalId) throws IOException {
		List<ProjectClosureTasks> props = new ArrayList<ProjectClosureTasks>();

		props.addAll(projClosureTasksRepo.findRequestsForProjectPropsal(proposalId));
		List<ProjectClosureTasksListItem> dtos = new ArrayList<>();
		props.forEach(q -> {
			ProjectClosureTasksListItem ppli = new ProjectClosureTasksListItem();

			ppli.setId(q.getId());
			ppli.setAssignee(new UserLookupItem(q.getAssignee().getId(), q.getAssignee().getFullName()));
			ppli.setStatus(q.getStatus());
			ppli.setInitiatedBy(new UserLookupItem(q.getInitiatedBy().getId(), q.getInitiatedBy().getFullName()));
			ppli.setGeneralFields(q.getGeneralFields());
			ppli.setCreated_at(q.getCreated_at());
			ppli.setAssigned(q.getAssignee().getId().equals(currentUser.getUserId()));
			ppli.setOrderNum(q.getOrderNum());

			dtos.add(ppli);
		});
		return dtos;
	}

	@Transactional
	public void submitProjectClosureTask(UUID currentUserId, UUID taskId, ProjectClosureTaskSubmitRequest body) throws IOException {
		ProjectClosureTasks props;
		props = projClosureTasksRepo.findById(taskId)
				.orElseThrow(() -> new ValidationException("INVALID TASK ID"));
		props.setGeneralFields(body.getData());
		props.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		if(props.getProjectClosureRef().getTasks().stream().allMatch(
				r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue())
		)){
			props.getProjectClosureRef().setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		}
	}
}