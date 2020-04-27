package com.ndrmf.setting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.setting.dto.AddDepartmentRequest;
import com.ndrmf.setting.dto.AddDesignationRequest;
import com.ndrmf.setting.dto.DepartmentItem;
import com.ndrmf.setting.dto.DesignationItem;
import com.ndrmf.setting.model.Department;
import com.ndrmf.setting.model.Designation;
import com.ndrmf.setting.repository.DepartmentRepository;
import com.ndrmf.setting.repository.DesignationRepository;

@Service
public class SettingService {
	@Autowired private DepartmentRepository deptRepo;
	@Autowired private DesignationRepository desigRepo;
	
	public void addDepartment(AddDepartmentRequest body) {
		Department d = new Department();
		d.setName(body.getName());
		
		deptRepo.save(d);
	}
	
	public List<DepartmentItem> getAllDepartments(){
		List<Department> ds = deptRepo.findAll();
		
		List<DepartmentItem> dtos = ds.stream().map(d -> new DepartmentItem(d.getId(), d.getName())).collect(Collectors.toList());
		
		return dtos;
	}
	
	public void addDesignation(AddDesignationRequest body) {
		Designation d = new Designation();
		d.setName(body.getName());
		
		desigRepo.save(d);
	}
	
	public List<DesignationItem> getAllDesignations(){
		List<Designation> ds = desigRepo.findAll();
		
		List<DesignationItem> dtos = ds.stream().map(d -> new DesignationItem(d.getId(), d.getName())).collect(Collectors.toList());
		
		return dtos;
	}
}
