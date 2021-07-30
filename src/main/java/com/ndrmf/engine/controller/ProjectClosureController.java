package com.ndrmf.engine.controller;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.Comment;
import com.ndrmf.engine.dto.ProjectClosureTaskSubmitRequest;
import com.ndrmf.engine.dto.ProjectClosureTasksListItem;
import com.ndrmf.engine.dto.CommenceProjectClosure;
import com.ndrmf.engine.service.ProjectClosureService;
import com.ndrmf.util.enums.ProcessStatus;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Api(tags = "Project Closure")
@RestController
@RequestMapping("/project-closure")
public class ProjectClosureController {
	@Autowired private ProjectClosureService projectClosureService;

	@GetMapping("/{proposalId}")
	public ResponseEntity<List<ProjectClosureTasksListItem>> getProjectClosures(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId", required = false) UUID proposalId) throws IOException{
		return new ResponseEntity<>(projectClosureService.getProjectClosureRequestsByProposalId(principal, proposalId), HttpStatus.OK);
	}

	@PostMapping("/commence")
	public ResponseEntity<?> commenceProjectClosureRequest(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody @Valid CommenceProjectClosure body
	){
		projectClosureService.commenceProjectClosure(principal, body);
		return new ResponseEntity<>(new ApiResponse(true, "Project closure initiated successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/{taskId}/submit")
	public ResponseEntity<ApiResponse> submitProjectClosureTask(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "taskId", required = true) UUID taskId,
			@RequestBody @Valid ProjectClosureTaskSubmitRequest body) throws IOException {
		projectClosureService.submitProjectClosureTask(principal.getUserId(), taskId, body);

		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Task submitted successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{requestId}/mark-to-ceo")
	public ResponseEntity<ApiResponse> markProjectClosureToCeo(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "requestId", required = true) UUID requestId) throws IOException {
		projectClosureService.markProjectClosureToCeo(principal.getUserId(), requestId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Assigned to ceo successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{requestId}/ceo/approval")
	public ResponseEntity<ApiResponse> projectClosureCeoApproval(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "requestId", required = true) UUID requestId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@RequestBody @Valid Comment body) throws IOException {
		projectClosureService.projectClosureCeoApproval(principal.getUserId(), requestId, body, status);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Assigned to ceo successfully."), HttpStatus.CREATED);
	}
}