package com.ndrmf.engine.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportItem;
import com.ndrmf.engine.dto.qpr.QuarterlyProgressReportListItem;
import com.ndrmf.engine.service.QPRService;

import io.swagger.annotations.Api;

@Api(tags = "M & E Process")
@RestController
public class MaintenanceAndExecutionController {
	@Autowired private QPRService qprService;
	
	@PostMapping("/qpr/commence")
	public ResponseEntity<?> commenceQPR(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "proposalId", required = true) UUID proposalId){
		Map<String, UUID> dto = new HashMap<>();
		dto.put("id", qprService.commenceQPR(proposalId));
	
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/qpr")
	public ResponseEntity<List<QuarterlyProgressReportListItem>> getQPRRequests(@AuthenticationPrincipal AuthPrincipal principal){
	
		return new ResponseEntity<>(qprService.getQPRRequests(principal), HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/qpr/{id}")
	public ResponseEntity<QuarterlyProgressReportItem> getProjectProposalRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		QuarterlyProgressReportItem dto = qprService.getQPRRequest(id, principal);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
}
