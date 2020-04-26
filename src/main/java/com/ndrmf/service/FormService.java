package com.ndrmf.service;

import org.springframework.http.ResponseEntity;

import com.ndrmf.request.FormCreationRequest;
import com.ndrmf.request.FormUpdationRequest;
import com.ndrmf.response.ServiceResponse;

public interface FormService {

    ResponseEntity<ServiceResponse> createForm(FormCreationRequest formCreationRequest);

    ResponseEntity<ServiceResponse> updateForm(FormUpdationRequest formUpdationRequest);

    ResponseEntity<ServiceResponse> getAllForms();

}
