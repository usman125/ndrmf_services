package com.ndrmf.engine.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.ndrmf.engine.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.qpr.QPRSectionRequest;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportItem;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportListItem;
import com.ndrmf.engine.service.QPRService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;

import io.swagger.annotations.Api;

@Api(tags = "M & E Process")
@RestController
public class MaintenanceAndExecutionController {
	@Autowired private QPRService qprService;
	
	@PostMapping("/qpr/commence")
	public ResponseEntity<?> commenceQPR(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody @Valid DateAndCommentBody body,
			@RequestParam(name = "proposalId", required = true) UUID proposalId){
		Map<String, UUID> dto = new HashMap<>();
		dto.put("id", qprService.commenceQPR(proposalId, body));
	
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/qpr")
	public ResponseEntity<List<QuarterlyProgressReportListItem>> getQPRRequests(@AuthenticationPrincipal AuthPrincipal principal){
	
		return new ResponseEntity<>(qprService.getQPRRequests(principal), HttpStatus.OK);
	}
	
	@GetMapping(path = "/qpr/{id}")
	public ResponseEntity<QuarterlyProgressReportItem> getQPRRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		QuarterlyProgressReportItem dto = qprService.getQPRRequest(id, principal);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/qpr/{id}/section/submit")
	public ResponseEntity<ApiResponse> submitSection(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "action", required = true) FormAction action,
			@PathVariable(name = "id", required = true) UUID id,
			@RequestBody @Valid QPRSectionRequest body){
		qprService.submitQPRSection(id, principal.getUserId(), body, action);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section saved successfully."), HttpStatus.OK);
	}

//	@PostMapping("/qualification/section/{sectionId}/review/add")
//	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
//												 @PathVariable(name = "sectionId", required = true) UUID sectionId,
//												 @RequestBody @Valid AddQualificationSectionReviewRequest body){
//
//		commentService.addQualificationSectionReview(principal.getUserId(), sectionId, body);
//
//		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
//	}

	@PostMapping("qpr/section/{sectionId}/task/add")
	public ResponseEntity<ApiResponse> addTaskForSection(@AuthenticationPrincipal AuthPrincipal principal,
														 @PathVariable(name = "sectionId", required = true) UUID sectionId,
														 @RequestBody @Valid AddQualificationTaskRequest body){
		qprService.addQprSectionTask(sectionId, principal.getUserId(), body);
		return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
	}

	@PostMapping("qpr/{qprId}/tasks/add")
	public ResponseEntity<ApiResponse> addTasksForQpr(@AuthenticationPrincipal AuthPrincipal principal,
														 @PathVariable(name = "qprId", required = true) UUID qprId,
														 @RequestBody @Valid AddQprTasksRequest body){
		qprService.addTasksForQpr(qprId, principal.getUserId(), body);
		return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
	}

	@PostMapping("qpr/section/{sectionId}/review/add")
	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
												 @PathVariable(name = "sectionId", required = true) UUID sectionId,
												 @RequestBody @Valid AddQprSectionReviewRequest body){

		qprService.addQprSectionReview(principal.getUserId(), sectionId, body);

		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}

	@PostMapping("qpr/{taskId}/review/add")
	public ResponseEntity<ApiResponse> addReviewForQprTask(@AuthenticationPrincipal AuthPrincipal principal,
												 @PathVariable(name = "taskId", required = true) UUID taskId,
												 @RequestBody @Valid AddQprTaskReviewRequest body){

		qprService.addQprReviewByDepUser(principal.getUserId(), taskId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}

	@PostMapping("qpr/{qprId}/extend/timeline")
	public ResponseEntity<ApiResponse> extendQprTimeline(@AuthenticationPrincipal AuthPrincipal principal,
														   @PathVariable(name = "qprId", required = true) UUID qprId,
														   @RequestBody @Valid DateAndCommentBody body){

		qprService.extendQprTimeline(principal.getUserId(), qprId, body);

		return new ResponseEntity<>(new ApiResponse(true, "Timeline extended successfully."), HttpStatus.OK);
	}
}
