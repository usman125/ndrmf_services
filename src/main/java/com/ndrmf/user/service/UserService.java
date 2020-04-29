package com.ndrmf.user.service;


import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;
import com.ndrmf.user.dto.UserItem;

public interface UserService {
    void createUser(CreateUserRequest body);
    
    void createSignup(SignupRequest body);
    
    List<SignupRequestItem> getPendingSignupRequests();
    
    List<OrganisationAndRoles> getOrganisations();
    
    List<UserItem> getActiveUsers();
    
    List<UserItem> getAllUsers();
    
    void approveSignupRequest(UUID id, String remarks);
    

    ResponseEntity<ServiceResponse> addRolesForUser(AddRoleUserRequest addRoleUserRequest);

    ResponseEntity<ServiceResponse> updateProfile(UserActivationRequest userActivationRequest);

    ResponseEntity<ServiceResponse> updateProfile(UserEligibilityRequest userEligibilityRequest);

    ResponseEntity<ServiceResponse> updateProfile(UserQualificationRequest userQualificationRequest);

    ResponseEntity<ServiceResponse> getUsers(String fetchOption);

    ResponseEntity<ServiceResponse> getUsersHavingRole(String roleName);
}
