package com.ndrmf.setting.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.setting.dto.AddDepartmentRequest;
import com.ndrmf.setting.dto.AddDesignationRequest;
import com.ndrmf.setting.dto.AddSectionRequest;
import com.ndrmf.setting.dto.AddSectionTemplateRequest;
import com.ndrmf.setting.dto.DepartmentItem;
import com.ndrmf.setting.dto.DesignationItem;
import com.ndrmf.setting.dto.ProcessTemplateItem;
import com.ndrmf.setting.dto.ProcessTypeWithSectionsItem;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.service.SettingService;
import com.ndrmf.setting.service.TemplateService;
import com.ndrmf.util.enums.ProcessType;

import io.swagger.annotations.Api;

@Api(tags = "Settings")
@RestController
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired private SettingService settingService;
	@Autowired private TemplateService templateService;
	
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
	
	@GetMapping("/process/types")
	public ResponseEntity<List<String>> getProcessTypes(){
		List<String> types = Stream.of(ProcessType.values())
				.map(ProcessType::name)
				.collect(Collectors.toList());
		
		return new ResponseEntity<List<String>>(types, HttpStatus.OK);
	}
	
	@GetMapping("/process/meta")
	public ResponseEntity<?> getAllProcessesMeta(){
		return new ResponseEntity<List<ProcessTypeWithSectionsItem>>(templateService.getAllProcessesMeta(), HttpStatus.OK);
	}
	
	@GetMapping("/process/{processType}/template")
	public ResponseEntity<ProcessTemplateItem> getTemplateForProcessType(@PathVariable(name = "processType", required = true) String processType){
		return new ResponseEntity<ProcessTemplateItem>(templateService.getTemplateForProcessType(processType), HttpStatus.OK);
	}
	
	@PostMapping("/process/{processType}/section/add")
	public ResponseEntity<ApiResponse> addSection(@PathVariable(name = "processType", required = true) String processType,
			@RequestBody @Valid AddSectionRequest body){
		templateService.addSectionForProcess(processType, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section added successfully"), HttpStatus.CREATED);
	}
	
	@PostMapping("/section/{sectionId}/template/add")
	public ResponseEntity<ApiResponse> addTemplateForSection(@PathVariable(name = "sectionId", required = true) UUID sectionId, @RequestBody @Valid AddSectionTemplateRequest body){
		templateService.addTemplateForSection(sectionId, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section Template added successfully"), HttpStatus.CREATED);
	}
}
