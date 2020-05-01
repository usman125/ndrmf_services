package com.ndrmf.setting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.setting.dto.AddDepartmentRequest;
import com.ndrmf.setting.dto.AddDesignationRequest;
import com.ndrmf.setting.dto.DepartmentItem;
import com.ndrmf.setting.dto.DesignationItem;
import com.ndrmf.setting.service.SettingService;

import io.swagger.annotations.Api;

@Api(tags = "Settings")
@RestController
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired private SettingService settingService;
	
	@GetMapping("/department")
	public ResponseEntity<List<DepartmentItem>> getAllDepartments(){
		return new ResponseEntity<List<DepartmentItem>>(settingService.getAllDepartments(), HttpStatus.OK);
	}
	
	@PostMapping("/department/add")
	public ResponseEntity<ApiResponse> addDepartment(@RequestBody AddDepartmentRequest body){
		settingService.addDepartment(body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Department added"), HttpStatus.OK);
	}
	
	@GetMapping("/designation")
	public ResponseEntity<List<DesignationItem>> getAllDesignations(){
		return new ResponseEntity<List<DesignationItem>>(settingService.getAllDesignations(), HttpStatus.OK);
	}
	
	@PostMapping("/designation/add")
	public ResponseEntity<ApiResponse> addDesignations(@RequestBody AddDesignationRequest body){
		settingService.addDesignation(body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Designation added"), HttpStatus.OK);
	}
}
