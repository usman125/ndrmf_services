package com.ndrmf.engine.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.CommenceProjectProposalRequest;
import com.ndrmf.engine.dto.ProjectProposalItem;
import com.ndrmf.engine.service.ProjectProposalService;
import com.ndrmf.util.constants.SystemRoles;

import io.swagger.annotations.Api;

@Api(tags = "Project Proposal Process")
@RestController
@RequestMapping("/project-proposal")
public class ProjectProposalController {
	@Autowired private ProjectProposalService projProposalService;
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/commence")
	public ResponseEntity<?> startQualificationRequest(@AuthenticationPrincipal AuthPrincipal principal,
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
}