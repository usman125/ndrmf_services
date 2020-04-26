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
import com.ndrmf.user.service.RoleService;
import com.ndrmf.user.service.UserService;
import com.ndrmf.utils.CommonConstants;

import io.swagger.annotations.Api;

import java.util.List;

import javax.validation.Valid;


@Api(tags = "User")
@RestController
@RequestMapping(value="/user")
public class UserController {
	@Autowired
    private UserService userService;
	@Autowired
    private RoleService roleService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest body){
    	userService.createUser(body);
    	
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User created successfully."), HttpStatus.CREATED);
    }
    
    @GetMapping("/orgs")
    public ResponseEntity<List<OrganisationAndRoles>> getOrganisations(){
    	List<OrganisationAndRoles> orgs = userService.getOrganisations();
    	
    	return new ResponseEntity<List<OrganisationAndRoles>>(orgs, HttpStatus.OK);
    }

    @PutMapping("/user/updateActiveStatus")
    public ResponseEntity<ServiceResponse> updateActiveStatus(@Valid @RequestBody UserActivationRequest userActivationRequest){
        return userService.updateProfile(userActivationRequest);
    }

    @PutMapping("/user/updateEligibleStatus")
    public ResponseEntity<ServiceResponse> updateEligibleStatus(@Valid @RequestBody UserEligibilityRequest userEligibilityRequest){
        return userService.updateProfile(userEligibilityRequest);
    }

    @PutMapping("/user/updateQualifiedStatus")
    public ResponseEntity<ServiceResponse> updateQualifiedStatus(@Valid @RequestBody UserQualificationRequest userQualificationRequest){
        return userService.updateProfile(userQualificationRequest);
    }

    @GetMapping("/user/getAllUsers")
    public ResponseEntity<ServiceResponse> getAllUsers(){
        return userService.getUsers(CommonConstants.FETCH_ALL_USER_OPTION);
    }

    @GetMapping("/user/getActiveUser")
    public ResponseEntity<ServiceResponse> getActiveUser(){
        return userService.getUsers(CommonConstants.FETCH_ACTIVE_USER_OPTION);
    }

    @GetMapping("/user/getInActiveUser")
    public ResponseEntity<ServiceResponse> getInActiveUser(){
        return userService.getUsers(CommonConstants.FETCH_INACTIVE_USER_OPTION);
    }

    @GetMapping("/user/getUsersOfRole/{roleName}")
    public ResponseEntity<ServiceResponse> getUserHavingRole(@PathVariable String roleName){
        return userService.getUsersHavingRole(roleName);
    }

    @PutMapping("/user/addRole")
    public ResponseEntity<ServiceResponse> addUserRoles(@Valid @RequestBody AddRoleUserRequest addRoleUserRequest){
        return userService.addRolesForUser(addRoleUserRequest);
    }

    @GetMapping("/user/getRoles")
    public ResponseEntity<ServiceResponse> getAllRoles(){
        return roleService.getAllRoles();
    }
}
