package com.ndrmf.engine.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.AddSubProjectSectionReviewRequest;
import com.ndrmf.engine.dto.SubProjectDocumentItem;
import com.ndrmf.engine.dto.SubProjectDocumentListItem;
import com.ndrmf.engine.dto.SubProjectDocumentSectionRequest;
import com.ndrmf.engine.service.ImplementationService;
import com.ndrmf.util.constants.SystemRoles;

import io.swagger.annotations.Api;

@Api(tags = "Project Implementation Process")
@RestController
@RequestMapping("/implementation")
public class ImplementationController {
	@Autowired private ImplementationService implService;
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/{proposalId}/sub-proj-doc/commence")
	public ResponseEntity<ApiResponse> commenceSubProjectDocument(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId", required = true) UUID proposalId){
		
		implService.commenceSubProjectDocument(proposalId);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Process Commenced Successfully."), HttpStatus.CREATED);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@GetMapping("/sub-proj-doc/pending")
	public ResponseEntity<List<SubProjectDocumentListItem>> getPendingSubProjectDocuments(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<>(implService.getPendingSubProjectDocuments(principal.getUserId()), HttpStatus.OK);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@GetMapping("/sub-proj-doc/{id}")
	public ResponseEntity<SubProjectDocumentItem> getSubProjectDocument(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<>(implService.getSubProjectDocument(id, principal.getUserId()), HttpStatus.OK);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/sub-proj-doc/{subProjectDocumentId}/section/submit")
	public ResponseEntity<ApiResponse> submitSubProjectDocumentSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "subProjectDocumentId", required = true) UUID id,
			SubProjectDocumentSectionRequest body){
		implService.submitSubProjectDocumentSection(id, principal.getUserId(), body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section submitted Successfully."), HttpStatus.CREATED);
	}
	
	@PutMapping("/sub-proj-doc/section/{sectionId}/request-review")
	public ResponseEntity<ApiResponse> requestSubProjectSectionForReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId){
		implService.requestSubProjectDocumentSectionReview(sectionId);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Review request submitted Successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/sub-proj-doc/section/{sectionId}/review/add")
	public ResponseEntity<ApiResponse> addSubProjectSectionReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddSubProjectSectionReviewRequest body){
		
		implService.submitSubProjectDocumentSectionReview(sectionId, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}
}
