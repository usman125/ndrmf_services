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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.CommenceProjectProposalRequest;
import com.ndrmf.engine.dto.PreliminaryAppraisalItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalListItem;
import com.ndrmf.engine.dto.PreliminaryAppraisalRequest;
import com.ndrmf.engine.dto.ProjectProposalItem;
import com.ndrmf.engine.dto.ProjectProposalListItem;
import com.ndrmf.engine.dto.ProjectProposalSectionRequest;
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
	@PostMapping("/{requestId}/section/add")
	public ResponseEntity<ApiResponse> submitSection(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "action", required = true) FormAction action,
			@PathVariable(name = "requestId", required = true) UUID requestId,
			@RequestBody @Valid ProjectProposalSectionRequest body){
		projProposalService.submitSection(principal.getUserId(), requestId, body, action);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section saved successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/pre-appraisal/add")
	public ResponseEntity<ApiResponse> addPreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody PreliminaryAppraisalRequest body){
		projProposalService.addPreliminaryAppraisal(principal.getUserId(), proposalId, body);
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
}