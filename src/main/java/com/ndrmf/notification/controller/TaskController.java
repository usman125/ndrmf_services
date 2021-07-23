package com.ndrmf.notification.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.notification.service.TaskService;

import io.swagger.annotations.Api;

@Api(tags = "Tasks And Notifications")
@RestController
@RequestMapping("/task")
public class TaskController {
	@Autowired private TaskService taskService;
	
	@GetMapping("/")
	public ResponseEntity<Map<String, List<TaskItem>>> getTasks(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<Map<String,List<TaskItem>>>(taskService.getAllTasks(principal.getUserId()), HttpStatus.OK);
	}
}