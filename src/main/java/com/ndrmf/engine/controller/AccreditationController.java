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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.AccreditationStatusItem;
import com.ndrmf.engine.dto.EligibilityListItem;
import com.ndrmf.engine.dto.EligibilityRequest;
import com.ndrmf.engine.dto.QualificationRequest;
import com.ndrmf.engine.service.AccreditationService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;

import io.swagger.annotations.Api;

@Api(tags = "Accreditation Process")
@RestController
@RequestMapping("/accreditation")
public class AccreditationController {
	
	@Autowired private AccreditationService accreditationService;
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@GetMapping("/status")
	public ResponseEntity<AccreditationStatusItem> getAccreditationStatus(@AuthenticationPrincipal AuthPrincipal principal){
		return new ResponseEntity<AccreditationStatusItem>(accreditationService.getAccreditationStatus(principal.getUserId()), HttpStatus.OK);
	}
	
	@GetMapping("/eligibility")
	public ResponseEntity<?> getAllEligibilityRequests(@AuthenticationPrincipal AuthPrincipal principal, @RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<List<EligibilityListItem>>(accreditationService.getEligibilityRequests(principal.getUserId(), status), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/eligibility/add")
	public ResponseEntity<ApiResponse> addEligibility(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody @Valid EligibilityRequest body){
		accreditationService.addEligibility(principal.getUserId(), body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request added successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/eligibility/{id}/approve")
	public ResponseEntity<ApiResponse> approveEligibility(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable(name = "id", required = true) UUID id){
		accreditationService.approveEligibilityRequest(id, principal.getUserId());
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request approved successfully."), HttpStatus.ACCEPTED);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/qualification/add")
	public ResponseEntity<ApiResponse> addQualification(@AuthenticationPrincipal AuthPrincipal principal, @RequestParam(name = "action", required = true) FormAction action, @RequestBody @Valid QualificationRequest body){
		accreditationService.addQualification(principal.getUserId(), body, action);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Qualification request added successfully."), HttpStatus.CREATED);
	}
}
