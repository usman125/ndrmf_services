package com.ndrmf.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.PagedList;
import com.ndrmf.notification.dto.NotificationItem;
import com.ndrmf.notification.service.NotificationService;

import io.swagger.annotations.Api;

@Api(tags = "Tasks And Notifications")
@RestController
@RequestMapping("/notifs")
public class NotificationController {
	
	@Autowired private NotificationService notService;
	
	@GetMapping("/")
	public ResponseEntity<PagedList<NotificationItem>>
	getNotifications(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "25") int pageSize){
		
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		PagedList<NotificationItem> dtos = notService.getNotifications(getCurrentUsername(), pageable);
		
		return new ResponseEntity<PagedList<NotificationItem>>(dtos, HttpStatus.OK);
	}
	
	private String getCurrentUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
