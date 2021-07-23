package com.ndrmf.engine.controller;

import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.ndrmf.engine.dto.*;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.service.ImplementationService;
import com.ndrmf.util.constants.SystemRoles;

import com.ndrmf.engine.service.CommentService;

import io.swagger.annotations.Api;

@Api(tags = "Project Implementation Process")
@RestController
@RequestMapping("/implementation")
public class ImplementationController {
	@Autowired private ImplementationService implService;
	@Autowired private CommentService commentService;
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/{proposalId}/sub-proj-doc/commence")
	public ResponseEntity<ApiResponse> commenceSubProjectDocument(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody @Valid CommenceSubProjectDocumentBody body,
			@PathVariable(name = "proposalId", required = true) UUID proposalId){
		
		implService.commenceSubProjectDocument(proposalId, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Process Commenced Successfully."), HttpStatus.CREATED);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@GetMapping("/sub-proj-doc/pending")
	public ResponseEntity<List<SubProjectDocumentListItem>> getPendingSubProjectDocuments(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<>(implService.getPendingSubProjectDocuments(principal.getUserId()), HttpStatus.OK);
	}
	
	@GetMapping("/sub-proj-doc")
	public ResponseEntity<List<SubProjectDocumentListItem>> getSubProjectDocuments(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<>(implService.getSubProjectDocuments(principal.getUserId()), HttpStatus.OK);
	}
	
	
	@GetMapping("/sub-proj-doc/{id}")
	public ResponseEntity<SubProjectDocumentItem> getSubProjectDocument(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<>(implService.getSubProjectDocument(id, principal.getUserId()), HttpStatus.OK);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/sub-proj-doc/{subProjectDocumentId}/section/submit")
	public ResponseEntity<ApiResponse> submitSubProjectDocumentSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "subProjectDocumentId", required = true) UUID id,
			@RequestBody SubProjectDocumentSectionRequest body){
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

	@PostMapping("/sub-proj-doc/{requestId}/assign/dmpam/tasks")
	public ResponseEntity<ApiResponse> addSubProjectDmPamTasks(@AuthenticationPrincipal AuthPrincipal principal,
																  @PathVariable(name = "requestId", required = true) UUID requestId,
																  @RequestBody @Valid AddQprTasksRequest body){

		implService.addSubProjectDmPamTasks(principal, requestId, body);

		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}

	@PostMapping("/sub-proj-doc/{requestId}/dmpam/assign/reviews")
	public ResponseEntity<ApiResponse> assignUserReviewsByDmpam(@AuthenticationPrincipal AuthPrincipal principal,
															   @PathVariable(name = "requestId", required = true) UUID requestId,
															   @RequestBody @Valid AddQprTasksRequest body){

		implService.assignUserReviewsByDmpam(principal, requestId, body);

		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}

	@GetMapping("/sub-proj-doc/dmpam/tasks")
	public ResponseEntity<List<SubProjectDocumentDmPamTasksListItem>> getSubProjectDmPamTasks(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<>(implService.getSubProjectDmPamTasks(principal), HttpStatus.OK);
	}

	@GetMapping("/sub-proj-doc/user/tasks")
	public ResponseEntity<List<SubProjectDocumentTasksListItem>> getSubProjectTasks(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<>(implService.getSubProjectTasks(principal), HttpStatus.OK);
	}

	@PostMapping("/sub-proj-doc/{id}/comment/add/{taskId}")
	public ResponseEntity<ApiResponse> addGeneralComments(@AuthenticationPrincipal AuthPrincipal principal,
														  @PathVariable(name = "id", required = true) UUID requestId,
														  @PathVariable(name = "taskId", required = true) UUID taskId,
														  @RequestBody @Valid AddProposalGeneralCommentRequest body){

		commentService.addSubProjectDocumentGeneralComment(requestId, taskId, principal, body);
		return new ResponseEntity<>(new ApiResponse(true, "Comment added successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/sub-proj-doc/{requestId}/reassign")
	public ResponseEntity<ApiResponse> reassignSubProjectDocumentToFIP(@AuthenticationPrincipal AuthPrincipal principal,
													 @PathVariable(name = "requestId") UUID requestId,
													 @RequestBody ReassignPrposalToFIPRequest body){
		implService.reassignSubProjectDocumentToFIP(requestId, principal.getUserId(), body.getSectionIds(), body.getComments());

		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Sections reassigned successfully"), HttpStatus.ACCEPTED);
	}

	@PutMapping("/sub-proj-doc/change/{requestId}/status")
	public ResponseEntity<ApiResponse> changeSubProjectDocStatus(
			@PathVariable(name = "requestId", required = true) UUID requestId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@AuthenticationPrincipal AuthPrincipal principal){

		implService.changeSubProjectDocStatus(requestId, principal, status);
		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
	}

	@PutMapping("/sub-proj-doc/change-dmpam-task/{requestId}/status")
	public ResponseEntity<ApiResponse> changeSubProjectDocDmPamTaskStatus(
			@PathVariable(name = "requestId", required = true) UUID requestId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@AuthenticationPrincipal AuthPrincipal principal){

		implService.changeSubProjectDocDmPamTaskStatus(requestId, principal, status);
		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
	}
}
