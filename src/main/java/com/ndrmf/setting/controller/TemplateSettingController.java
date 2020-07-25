package com.ndrmf.setting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.setting.dto.AddSectionRequest;
import com.ndrmf.setting.dto.AddSectionTemplateRequest;
import com.ndrmf.setting.dto.ProcessTemplateItem;
import com.ndrmf.setting.dto.ProcessTypeWithSectionsItem;
import com.ndrmf.setting.dto.UpdateProcessMetaRequest;
import com.ndrmf.setting.service.TemplateService;
import com.ndrmf.util.enums.ProcessType;

import io.swagger.annotations.Api;

@Api(tags = "Template Settings")
@RestController
@RequestMapping("/setting")
public class TemplateSettingController {
	@Autowired private TemplateService templateService;
	
	@GetMapping("/process/types")
	public ResponseEntity<List<String>> getProcessTypes(){
		List<String> types = Stream.of(ProcessType.values())
				.map(ProcessType::name)
				.collect(Collectors.toList());
		
		return new ResponseEntity<List<String>>(types, HttpStatus.OK);
	}
	
	@GetMapping("/process/{processType}/meta")
	public ResponseEntity<ProcessTypeWithSectionsItem> getProcessesMeta(@PathVariable(name = "processType", required = true) String processType){
		return new ResponseEntity<ProcessTypeWithSectionsItem>(templateService.getMetaForProcess(processType), HttpStatus.OK);
	}
	
	@PutMapping("/process/{processType}/meta")
	public ResponseEntity<ApiResponse> updateProcessMeta(@PathVariable(name = "processType", required = false) String processType, @Valid @RequestBody UpdateProcessMetaRequest body){
		templateService.updateProcessMeta(processType, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Process Meta updated successfully."), HttpStatus.OK);
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
	
	@PostMapping("/process/{processType}/sub-process-type/add")
	public ResponseEntity<Map<String, String>> addSubProcessType(@PathVariable(name = "processType", required = true) ProcessType processType,
			@RequestParam(name = "name", required = true) String name){
		String id = templateService.addSubProcessType(processType, name);
		
		Map<String, String> dto = new HashMap<>();
		dto.put("id", id);
		
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/process/{processType}/sub-process-type")
	public ResponseEntity<Set<String>> getSubProcessTypes(@PathVariable(name = "processType", required = true) ProcessType processType){
		return new ResponseEntity<>(templateService.getSubProcessTypesForProcess(processType), HttpStatus.OK);
	}
}
