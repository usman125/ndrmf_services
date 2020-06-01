package com.ndrmf.engine.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.AddProposalGeneralCommentRequest;
import com.ndrmf.engine.dto.AddProposalSectionReviewRequest;
import com.ndrmf.engine.dto.AddProposalTaskRequest;
import com.ndrmf.engine.dto.CommenceExtendedAppraisalRequest;
import com.ndrmf.engine.dto.CommencePreliminaryAppraisalRequest;
import com.ndrmf.engine.dto.CommenceProjectProposalRequest;
import com.ndrmf.engine.dto.ExtendedAppraisalItem;
import com.ndrmf.engine.dto.ExtendedAppraisalSectionRequest;
import com.ndrmf.engine.dto.PreliminaryAppraisalItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalListItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalRequest;
import com.ndrmf.engine.dto.ProjectProposalItem;
import com.ndrmf.engine.dto.ProjectProposalListItem;
import com.ndrmf.engine.dto.ProjectProposalSectionRequest;
import com.ndrmf.engine.service.CommentService;
import com.ndrmf.engine.service.ProjectProposalService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;

import io.swagger.annotations.Api;

@Api(tags = "Project Proposal Process")
@RestController
@RequestMapping("/project-proposal")
public class ProjectProposalController {
	@Autowired private ProjectProposalService projProposalService;
	@Autowired private CommentService commentService;
	
	@GetMapping("/")
	public ResponseEntity<List<ProjectProposalListItem>> getProjectProposals(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<>(projProposalService.getProjectProposalRequests(principal.getUserId(), status), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/commence")
	public ResponseEntity<?> commentProjectProposalRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody CommenceProjectProposalRequest body){
		Map<String, UUID> dto = new HashMap<>();
		dto.put("id", projProposalService.commenceProjectProposal(principal.getUserId(), body));
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProjectProposalItem> getProjectProposalRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<>(projProposalService.getProjectProposalRequest(id, principal.getUserId()), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/{proposalId}/section/add")
	public ResponseEntity<ApiResponse> submitSection(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "action", required = true) FormAction action,
			@PathVariable(name = "proposalId", required = true) UUID proposalId,
			@RequestBody @Valid ProjectProposalSectionRequest body){
		projProposalService.submitProposalSection(principal.getUserId(), proposalId, body, action);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section saved successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/pre-appraisal/commence")
	public ResponseEntity<ApiResponse> commencePreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody CommencePreliminaryAppraisalRequest body){
		projProposalService.commencePreliminaryAppraisal(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Pre-Appraisal Commenced successfully"), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/pre-appraisal/submit")
	public ResponseEntity<ApiResponse> addPreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody PreliminaryAppraisalRequest body){
		projProposalService.submitPreliminaryAppraisal(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Pre-Appraisal added successfully"), HttpStatus.CREATED);
	}
	
	@GetMapping("/pre-appraisal")
	public ResponseEntity<List<PreliminaryAppraisalListItem>> getPreliminaryAppraisals(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<>(projProposalService.getAllPreliminaryAppraisals(principal.getUserId(), status), HttpStatus.OK);
	}
	
	@GetMapping("/pre-appraisal/{id}")
	public ResponseEntity<PreliminaryAppraisalItem> getPreliminaryAppraisal(@PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<>(projProposalService.getPreliminaryAppraisal(id), HttpStatus.OK);
	}
	
	@PostMapping("/{proposalId}/ext-appraisal/commence")
	public ResponseEntity<ExtendedAppraisalItem> commenceExtendedAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody CommenceExtendedAppraisalRequest body){
		
		ExtendedAppraisalItem dto = projProposalService.commenceExtendedAppraisal(principal.getUserId(), proposalId, body);
		
		return new ResponseEntity<ExtendedAppraisalItem>(dto, HttpStatus.CREATED);
	}
	
	@PostMapping("/ext-appraisal/{extendedAppraisalId}/section/submit")
	public ResponseEntity<ApiResponse> submitExtendedAppraisalSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId,
			@RequestBody ExtendedAppraisalSectionRequest body){
		
		projProposalService.submitExtendedAppraisalSection(principal.getUserId(), extendedAppraisalId, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Section submitted successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/section/{sectionId}/task/add")
	public ResponseEntity<ApiResponse> addTaskForProposalSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddProposalTaskRequest body){
		projProposalService.addProjectProposalTask(sectionId, principal.getUserId(), body);
		return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/section/{sectionId}/review/add")
	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddProposalSectionReviewRequest body){
		
		commentService.addProjectProposalSectionReview(principal.getUserId(), sectionId, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/comment/add")
	public ResponseEntity<ApiResponse> addGeneralComments(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestBody @Valid AddProposalGeneralCommentRequest body){
		
		commentService.addProjectProposalGeneralComment(proposalId, principal, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Comment added successfully."), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProposalStatus(@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@AuthenticationPrincipal AuthPrincipal principal){
		
		projProposalService.updateProposalStatus(proposalId, principal.getUserId(), status);
	
		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
	}
}