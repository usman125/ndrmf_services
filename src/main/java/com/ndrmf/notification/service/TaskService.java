package com.ndrmf.notification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.model.ProjectProposalTask;
import com.ndrmf.engine.model.QualificationTask;
import com.ndrmf.engine.repository.ProjectProposalTaskRepository;
import com.ndrmf.engine.repository.QualificationTaskRepository;
import com.ndrmf.notification.dto.TaskItem;

@Service
public class TaskService {
	@Autowired private QualificationTaskRepository qTaskRepo;
	@Autowired private ProjectProposalTaskRepository proposalTaskRepo;
	
	public Map<String, List<TaskItem>> getAllTasks(UUID userId) {
		Map<String, List<TaskItem>> dto = new HashMap<String, List<TaskItem>>();
		
		List<QualificationTask> qTasks = qTaskRepo.findAllTasksForAssignee(userId);
		List<TaskItem> qualTasks = new ArrayList<TaskItem>();
		
		qTasks.forEach(t -> {
			TaskItem ti = new TaskItem();			
			ti.setComments(t.getComments());
			ti.setEndDate(t.getEndDate());
			ti.setRequestId(t.getSection().getQualifcationRef().getId());
			ti.setStartDate(t.getStartDate());
			ti.setTaskId(t.getId());
			ti.setSectionId(t.getSection().getId());
			ti.setSectionName(t.getSection().getName());
			ti.setStatus(t.getStatus());
			if(t.getQualification() != null) {
				ti.setFipName(t.getQualification().getInitiatedBy().getFullName());	
			}
			
			qualTasks.add(ti);
		});
		
		dto.put("qualification", qualTasks);
		
		List<ProjectProposalTask> pTasks = proposalTaskRepo.findAllTasksForAssignee(userId);
		List<TaskItem> proposalTasks = new ArrayList<TaskItem>();
		
		pTasks.forEach(t -> {
			TaskItem ti = new TaskItem();
			
			ti.setComments(t.getComments());
			ti.setEndDate(t.getEndDate());
			ti.setRequestId(t.getSection().getProposalRef().getId());
			ti.setStartDate(t.getStartDate());
			ti.setTaskId(t.getId());
			ti.setSectionId(t.getSection().getId());
			ti.setSectionName(t.getSection().getName());
			ti.setStatus(t.getStatus());
			if(t.getProposal() != null) {
				ti.setFipName(t.getProposal().getInitiatedBy().getFullName());	
			}
			
			proposalTasks.add(ti);
		});
		
		dto.put("proposal", proposalTasks);
		
		return dto;
	}
}
