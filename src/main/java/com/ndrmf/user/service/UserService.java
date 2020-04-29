package com.ndrmf.user.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;

public interface UserService {
    void createUser(CreateUserRequest body);
    
    void createSignup(SignupRequest body);
    
    List<SignupRequestItem> getPendingSignupRequests();
    
    List<OrganisationAndRoles> getOrganisations();

    ResponseEntity<ServiceResponse> addRolesForUser(AddRoleUserRequest addRoleUserRequest);

    ResponseEntity<ServiceResponse> updateProfile(UserActivationRequest userActivationRequest);

    ResponseEntity<ServiceResponse> updateProfile(UserEligibilityRequest userEligibilityRequest);

    ResponseEntity<ServiceResponse> updateProfile(UserQualificationRequest userQualificationRequest);

    ResponseEntity<ServiceResponse> getUsers(String fetchOption);

    ResponseEntity<ServiceResponse> getUsersHavingRole(String roleName);
}
