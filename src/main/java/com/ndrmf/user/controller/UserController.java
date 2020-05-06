package com.ndrmf.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.RoleItem;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;
import com.ndrmf.user.dto.UserItem;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.constants.SystemRoles;

import io.swagger.annotations.Api;

import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;


@Api(tags = "User")
@RestController
@RequestMapping(value="/user")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    private UserService userService;

	@RolesAllowed("ADMIN")
	@GetMapping("/")
    public ResponseEntity<List<UserItem>> getAllUsers(){
		return new ResponseEntity<List<UserItem>>(userService.getAllUsers(), HttpStatus.OK);
    }

	@RolesAllowed("ADMIN")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest body){
    	userService.createUser(body);
    	
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User created successfully."), HttpStatus.CREATED);
    }
	
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest body){
    	userService.createSignup(body);
    	
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Signup request created successfully."), HttpStatus.CREATED);
    }
    
    @RolesAllowed("ADMIN")
    @GetMapping("/signup/requests/pending")
    public ResponseEntity<List<SignupRequestItem>> getPendingSignupRequests(){
    	return new ResponseEntity<List<SignupRequestItem>>(userService.getPendingSignupRequests(), HttpStatus.OK);
    }
    
    @RolesAllowed("ADMIN")
    @GetMapping("/signup/requests/{id}/approve")
    public ResponseEntity<ApiResponse> approveSignupRequest(@PathVariable(name = "id", required = true) UUID id){
    	
    	logger.debug("Approving Signup Request with ID: "+id.toString());
    	
    	userService.approveSignupRequest(id, "approving request");
    	
    	return new ResponseEntity<ApiResponse>(new ApiResponse(true, "signup request approved"), HttpStatus.OK);
    }
    
    @GetMapping("/orgs")
    public ResponseEntity<List<OrganisationAndRoles>> getOrganisations(){
    	List<OrganisationAndRoles> orgs = userService.getOrganisations();
    	
    	return new ResponseEntity<List<OrganisationAndRoles>>(orgs, HttpStatus.OK);
    }


    @GetMapping("/getActiveUser")
    public ResponseEntity<List<UserItem>> getActiveUser(){
    	return new ResponseEntity<List<UserItem>>(userService.getActiveUsers(), HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/withRoleprocessOwner")
    public ResponseEntity<List<UserLookupItem>> getUserHavingRoleProcessOwner(){
        return new ResponseEntity<List<UserLookupItem>>(userService.getActiveUsersForLookupByRole(SystemRoles.PROCESS_OWNER), HttpStatus.OK);
    }
    
    @RolesAllowed("ADMIN")
    @GetMapping("/withRoleSME")
    public ResponseEntity<List<UserLookupItem>> getUserHavingRoleSME(){
        return new ResponseEntity<List<UserLookupItem>>(userService.getActiveUsersForLookupByRole(SystemRoles.SME), HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/role")
    public ResponseEntity<List<RoleItem>> getAllRoles(@RequestParam(name = "orgId", required = true) int orgId){
        return new ResponseEntity<List<RoleItem>>(userService.getRolesForOrganisation(orgId), HttpStatus.OK);
    }
}
