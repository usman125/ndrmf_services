package com.ndrmf.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ndrmf.model.Form;
import com.ndrmf.repository.FormRepository;
import com.ndrmf.request.FormCreationRequest;
import com.ndrmf.request.FormUpdationRequest;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.setting.model.Section;
import com.ndrmf.setting.repository.SectionRepository;
import com.ndrmf.util.CommonConstants;
import com.ndrmf.util.CommonUtils;

import java.util.List;
import java.util.UUID;

@Service
public class FormServiceImpl implements FormService {

    private final SectionRepository sectionRepository;
    private final FormRepository formRepository;

    public FormServiceImpl(SectionRepository sectionRepository, FormRepository formRepository){
        this.sectionRepository = sectionRepository;
        this.formRepository = formRepository;
    }

    @Override
    public ResponseEntity<ServiceResponse> createForm(FormCreationRequest formCreationRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils.isInvalidFormCreationRequest(formCreationRequest)){
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Section section = sectionRepository.findById(UUID.fromString(formCreationRequest.getSectionKey())).get();
            if (null == section){
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                Form form = CommonUtils.mapFormCreationRequest(formCreationRequest, section);
                formRepository.save(form);
                serviceResponse = CommonUtils.successResponse(CommonConstants.FORM_CREATION_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> updateForm(FormUpdationRequest formUpdationRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if(CommonUtils.isInvalidFormUpdationRequest(formUpdationRequest)){
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Section section = sectionRepository.findById(UUID.fromString(formUpdationRequest.getSectionKey())).get();
            if (null == section){
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                Form form = formRepository.findBySection(section);
                if (null == form){
                    serviceResponse = CommonUtils.dataNotFoundResponse(null);
                    responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
                } else {
                    formRepository.save(form);
                    serviceResponse = null;//CommonUtils.mapFormUpdateResponse(form);
                    responseEntity = ResponseEntity.ok(serviceResponse);
                }
            }
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ServiceResponse> getAllForms() {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;
        List<Form> formList;

        formList = formRepository.findAllByActiveTrue();
        if(CommonUtils.isNullOrEmptyCollection(formList)){
            serviceResponse = CommonUtils.dataNotFoundResponse(null);
        } else {
            serviceResponse = null;//CommonUtils.mapFormResponse(formList);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);

        return responseEntity;
    }
}
