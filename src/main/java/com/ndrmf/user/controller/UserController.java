package com.ndrmf.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;
import com.ndrmf.user.dto.UserItem;
import com.ndrmf.user.service.RoleService;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.CommonConstants;

import io.swagger.annotations.Api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;


@Api(tags = "User")
@RestController
@RequestMapping(value="/user")
public class UserController {
	@Autowired
    private UserService userService;
	@Autowired
    private RoleService roleService;

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
    
    @GetMapping("/orgs")
    public ResponseEntity<List<OrganisationAndRoles>> getOrganisations(){
    	List<OrganisationAndRoles> orgs = userService.getOrganisations();
    	
    	return new ResponseEntity<List<OrganisationAndRoles>>(orgs, HttpStatus.OK);
    }

    @PutMapping("/updateActiveStatus")
    public ResponseEntity<ServiceResponse> updateActiveStatus(@Valid @RequestBody UserActivationRequest userActivationRequest){
        return userService.updateProfile(userActivationRequest);
    }

    @PutMapping("/updateEligibleStatus")
    public ResponseEntity<ServiceResponse> updateEligibleStatus(@Valid @RequestBody UserEligibilityRequest userEligibilityRequest){
        return userService.updateProfile(userEligibilityRequest);
    }

    @PutMapping("/updateQualifiedStatus")
    public ResponseEntity<ServiceResponse> updateQualifiedStatus(@Valid @RequestBody UserQualificationRequest userQualificationRequest){
        return userService.updateProfile(userQualificationRequest);
    }

    @GetMapping("/getActiveUser")
    public ResponseEntity<List<UserItem>> getActiveUser(){
    	return new ResponseEntity<List<UserItem>>(userService.getActiveUsers(), HttpStatus.OK);
    }

    @GetMapping("/getInActiveUser")
    public ResponseEntity<ServiceResponse> getInActiveUser(){
        return userService.getUsers(CommonConstants.FETCH_INACTIVE_USER_OPTION);
    }

    @GetMapping("/getUsersOfRole/{roleName}")
    public ResponseEntity<ServiceResponse> getUserHavingRole(@PathVariable String roleName){
        return userService.getUsersHavingRole(roleName);
    }

    @PutMapping("/addRole")
    public ResponseEntity<ServiceResponse> addUserRoles(@Valid @RequestBody AddRoleUserRequest addRoleUserRequest){
        return userService.addRolesForUser(addRoleUserRequest);
    }

    @GetMapping("/getRoles")
    public ResponseEntity<ServiceResponse> getAllRoles(){
        return roleService.getAllRoles();
    }
}
