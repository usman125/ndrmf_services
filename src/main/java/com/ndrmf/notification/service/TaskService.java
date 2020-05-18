package com.ndrmf.notification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.model.QualificationTask;
import com.ndrmf.engine.repository.QualificationTaskRepository;
import com.ndrmf.notification.dto.TaskItem;

@Service
public class TaskService {
	@Autowired private QualificationTaskRepository qTaskRepo;
	
	public Map<String, List<TaskItem>> getAllTasks(UUID userId) {
		List<QualificationTask> qTasks = qTaskRepo.findAllTasksForAssignee(userId);
		
		Map<String, List<TaskItem>> dto = new HashMap<String, List<TaskItem>>();
		
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
		
		return dto;
	}
}
