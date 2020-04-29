package com.ndrmf.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ndrmf.response.RoleResponse;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.user.model.Role;
import com.ndrmf.user.repository.RoleRepository;
import com.ndrmf.util.CommonConstants;
import com.ndrmf.util.CommonUtils;
import com.ndrmf.util.ResponseCode;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<ServiceResponse> getAllRoles() {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse = new RoleResponse();

        List<Role> roles = roleRepository.findAll();
        if(CommonUtils.isNullOrEmptyCollection(roles)){
            serviceResponse.setResponseCode(ResponseCode.SUCCESS.getRespCode());
            serviceResponse.setResponseDesc(CommonConstants.DATA_NOT_FOUND_DESC);
        } else {
            serviceResponse = CommonUtils.mapRoleResponse(roles);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);

        return responseEntity;
    }
}
