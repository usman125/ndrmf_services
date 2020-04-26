package com.ndrmf.user.service;

import org.springframework.http.ResponseEntity;

import com.ndrmf.response.ServiceResponse;

public interface RoleService {
    ResponseEntity<ServiceResponse> getAllRoles();
}
