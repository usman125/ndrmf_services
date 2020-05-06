package com.ndrmf.engine.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.engine.dto.EligibilityListItem;
import com.ndrmf.engine.dto.EligibilityRequest;
import com.ndrmf.engine.service.AccreditationService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.ProcessStatus;

import io.swagger.annotations.Api;

@Api(tags = "Accreditation Process")
@RestController
@RequestMapping("/accreditation")
public class AccreditationController {
	
	@Autowired private AccreditationService accreditationService;
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@GetMapping("/status")
	public ResponseEntity<?> getAccreditationStatus(){
		Set<String> roles = this.getCurrentUserRoles();
		
		Map<String, Object> res = new HashMap<String, Object>();
		
		if(roles.contains(SystemRoles.ACCREDITED)) {
			res.put("accredited", true);
			res.put("eligibility", true);
			res.put("qualification", true);
		}
		else {
			res.put("accredited", false);
			res.put("eligibility", true);
			res.put("qualification", false);
		}
		
		return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
	}
	
	@GetMapping("/eligibility")
	public ResponseEntity<?> getAllEligibilityRequests(@RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<List<EligibilityListItem>>(accreditationService.getRequests(getCurrentUsername(), status), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ORG_FIP)
	@PostMapping("/eligibility/add")
	public ResponseEntity<ApiResponse> addEligibility(@RequestBody @Valid EligibilityRequest body){
		accreditationService.addEligibility(getCurrentUsername(), body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Eligibility request added successfully."), HttpStatus.CREATED);
	}
	
	private String getCurrentUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	private Set<String> getCurrentUserRoles(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth.getAuthorities() == null) {
			return Collections.emptySet();
		}
		
		Set<String> userRoles = auth.getAuthorities()
				.stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		
		return userRoles;
	}
}
