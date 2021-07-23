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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.service.AccreditationService;
import com.ndrmf.engine.service.CommentService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;

import io.swagger.annotations.Api;

@Api(tags = "Accreditation Process")
@RestController
@RequestMapping("/accreditation")
public class AccreditationController {
	
	@Autowired private AccreditationService accreditationService;
	@Autowired private CommentService commentService;
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@GetMapping("/status")
	public ResponseEntity<Object> getAccreditationStatus(@AuthenticationPrincipal AuthPrincipal principal){
		//return type was AccreditationStatusItem
		return new ResponseEntity<Object>(accreditationService.getAccreditationStatus(principal.getUserId(), principal.getRoles()), HttpStatus.OK);
	}
	
	@GetMapping("/eligibility")
	public ResponseEntity<List<EligibilityListItem>> getAllEligibilityRequests(@AuthenticationPrincipal AuthPrincipal principal, @RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<List<EligibilityListItem>>(accreditationService.getEligibilityRequests(principal.getUserId(), status), HttpStatus.OK);
	}
	
	@GetMapping("/eligibility/{id}")
	public ResponseEntity<EligibilityItem> getEligibilityRequest(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<EligibilityItem>(accreditationService.getEligibilityRequest(id, principal.getUserId()), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/eligibility/add")
	public ResponseEntity<ApiResponse> addEligibility(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody @Valid EligibilityRequest body){
		accreditationService.addEligibility(principal.getUserId(), body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request added successfully."), HttpStatus.CREATED);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PutMapping("/eligibility/{eligId}")
	public ResponseEntity<ApiResponse> updateEligibility(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody @Valid EligibilityRequest body,
			@PathVariable(name = "eligId", required = true) UUID eligId){
		accreditationService.updateEligibility(principal.getUserId(), eligId, body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request updated successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/eligibility/{id}/approve")
	public ResponseEntity<ApiResponse> approveEligibility(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable(name = "id", required = true) UUID id){
		accreditationService.approveEligibilityRequest(id, principal.getUserId());
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request approved successfully."), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/eligibility/{id}/reject")
	public ResponseEntity<ApiResponse> rejectEligibility(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable(name = "id", required = true) UUID id,
			@RequestBody @Valid Comment body){
		accreditationService.rejectEligibilityRequest(id, principal.getUserId(), body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request rejected successfully."), HttpStatus.OK);
	}
	
	@GetMapping("/qualification")
	public ResponseEntity<?> getAllQualificationRequests(@AuthenticationPrincipal AuthPrincipal principal, @RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<List<QualificationListItem>>(accreditationService.getQualificationRequests(principal.getUserId(), status), HttpStatus.OK);
	}
	
	@PutMapping("/qualification/{id}")
	public ResponseEntity<ApiResponse> updateQualificationStatus(@PathVariable(name = "id", required = true) UUID id,
			@RequestBody(required = false) @Valid QualificationItem body){
		
		accreditationService.updateQualificationStatus(id, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Qualification status updated successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/qualification/{id}/addReviewUsers")
	public ResponseEntity<ApiResponse> addQualificationReviewUsers(@PathVariable(name = "id", required = true) UUID id,
																 @RequestBody(required = false) @Valid QualificationReviewUsersRequest body){
		accreditationService.addQualificationReviewUsers(id, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Qualification review users added successfully."), HttpStatus.OK);
	}
	
	@GetMapping("/qualification/{id}")
	public ResponseEntity<EligPlusQual> getQualificationRequest(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<EligPlusQual>(accreditationService.getQualificationRequest(id, principal.getUserId()), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@GetMapping("/qualification/commence")
	public ResponseEntity<?> startQualificationRequest(@AuthenticationPrincipal AuthPrincipal principal){
		Map<String, UUID> dto = new HashMap<>();
		dto.put("id", accreditationService.commenceQualification(principal.getUserId()));
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/qualification/{requestId}/section/add")
	public ResponseEntity<ApiResponse> addQualification(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "action", required = true) FormAction action,
			@PathVariable(name = "requestId", required = true) UUID requestId,
			@RequestBody @Valid QualificationSectionRequest body){
		accreditationService.addQualificationSection(principal.getUserId(), requestId, body, action);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Qualification request added successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/qualification/section/{sectionId}/review/add")
	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddQualificationSectionReviewRequest body){
		
		commentService.addQualificationSectionReview(principal.getUserId(), sectionId, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}
	
	@PostMapping("qualification/section/{sectionId}/task/add")
	public ResponseEntity<ApiResponse> addTaskForSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddQualificationTaskRequest body){
		accreditationService.addQualificationTask(sectionId, principal.getUserId(), body);
		return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
	}
	
	@PostMapping("qualification/{id}/reassign")
	public ResponseEntity<ApiResponse> reassignQualificationRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id,
			@RequestBody ReassignQualificationRequest body){
		
		accreditationService.reassignQualificationRequest(id, principal.getUserId(), body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Qualification re-assigned successfully."), HttpStatus.OK);
	}
	
	@GetMapping("questionairre")
	public ResponseEntity<List<AccreditationQuestionairreListItem>> getAllQuestionairreRequests(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<List<AccreditationQuestionairreListItem>>(accreditationService.getAllQuestionairreRequests(principal.getUserId()), HttpStatus.OK);
	}
	
	@GetMapping("questionairre/{id}")
	public ResponseEntity<AccreditationQuestionairreItem> getQuestionairreRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<AccreditationQuestionairreItem>(accreditationService.getQuestionairreRequest(principal.getUserId(), id), HttpStatus.OK);
	}
	
	@GetMapping("questionairre/pending")
	public ResponseEntity<List<AccreditationQuestionairreListItem>> getPendingQuestionairres(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<List<AccreditationQuestionairreListItem>>(accreditationService.getPendingQuestionairres(principal.getUserId()), HttpStatus.OK);
	}
	
	@PostMapping("questionairre/{id}/submit")
	public ResponseEntity<ApiResponse> submitAccreditationQuestionairre(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody SubmitAccreditationQuestionairreRequest body,
			@PathVariable(name = "id", required=true) UUID id){
		accreditationService.submitAccreditationQuestionairre(principal.getUserId(), id, body);
		return new ResponseEntity<>(new ApiResponse(true, "Questionairre Submitted Successfully. FIP also Approved."), HttpStatus.OK);
	}
}
