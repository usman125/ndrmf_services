package com.ndrmf.setting.controller;

import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
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
import com.ndrmf.engine.dto.AddCostHeadRequest;
import com.ndrmf.setting.dto.AddDepartmentRequest;
import com.ndrmf.setting.dto.AddDesignationRequest;
import com.ndrmf.setting.dto.AddThematicAreaRequest;
import com.ndrmf.setting.dto.CostHeadItem;
import com.ndrmf.setting.dto.DepartmentItem;
import com.ndrmf.setting.dto.DesignationItem;
import com.ndrmf.setting.dto.ThematicAreaItem;
import com.ndrmf.setting.dto.UpdateDepartmentRequest;
import com.ndrmf.setting.service.SettingService;
import com.ndrmf.util.constants.SystemRoles;

import io.swagger.annotations.Api;

@Api(tags = "Settings")
@RestController
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired private SettingService settingService;
	
	@GetMapping("/department")
	public ResponseEntity<List<DepartmentItem>> getAllDepartments(@RequestParam(name = "enabled", required = false) Boolean enabled){
		return new ResponseEntity<List<DepartmentItem>>(settingService.getAllDepartments(enabled), HttpStatus.OK);
	}
	
	@PostMapping("/department/add")
	public ResponseEntity<ApiResponse> addDepartment(@RequestBody AddDepartmentRequest body){
		settingService.addDepartment(body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Department added"), HttpStatus.OK);
	}
	
	@PutMapping("/department/{id}")
	public ResponseEntity<ApiResponse> updateDepartment(@RequestBody UpdateDepartmentRequest body,
			@PathVariable(name = "id", required = true) int id){
		settingService.updateDepartment(id, body);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Department updated"), HttpStatus.OK);
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
	
	@GetMapping("/thematic-area")
	public ResponseEntity<List<ThematicAreaItem>> getAllThematicAreas(){
		
		return new ResponseEntity<>(settingService.getAllThematicAreas(), HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ADMIN)
	@PostMapping("/thematic-area/add")
	public ResponseEntity<ThematicAreaItem> addThematicArea(@RequestBody @Valid AddThematicAreaRequest body){
		ThematicAreaItem dto = settingService.addThematicArea(body);
		
		return new ResponseEntity<ThematicAreaItem>(dto, HttpStatus.CREATED);
	}
	
	@RolesAllowed(SystemRoles.ADMIN)
	@PutMapping("/thematic-area/{id}/update")
	public ResponseEntity<ApiResponse> updateThematicArea(@PathVariable(name = "id", required = true) UUID id,
			@RequestBody @Valid AddThematicAreaRequest body){
		settingService.udpateThematicArea(id, body);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Thematic Area updated"), HttpStatus.CREATED);
	}
	
	@GetMapping("/cost-head")
	public ResponseEntity<List<CostHeadItem>> getCostHeads(@RequestParam(name = "enabled", required = false) Boolean enabled){
		List<CostHeadItem> dtos = settingService.findAllCostHeads(enabled == null? true : enabled);
		
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
	
	@RolesAllowed(SystemRoles.ADMIN)
	@PostMapping("/cost-head/add")
	public ResponseEntity<ApiResponse> addCostHead(@RequestBody @Valid AddCostHeadRequest body){
		settingService.addCostHead(body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Cost head addedd successfully"), HttpStatus.CREATED);
	}
}
