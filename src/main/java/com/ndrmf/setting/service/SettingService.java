package com.ndrmf.setting.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.dto.AddDepartmentRequest;
import com.ndrmf.setting.dto.AddDesignationRequest;
import com.ndrmf.setting.dto.AddThematicAreaRequest;
import com.ndrmf.setting.dto.DepartmentItem;
import com.ndrmf.setting.dto.DesignationItem;
import com.ndrmf.setting.dto.ThematicAreaItem;
import com.ndrmf.setting.model.Department;
import com.ndrmf.setting.model.Designation;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.setting.repository.DepartmentRepository;
import com.ndrmf.setting.repository.DesignationRepository;
import com.ndrmf.setting.repository.ThematicAreaRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.repository.UserRepository;

@Service
public class SettingService {
	@Autowired private DepartmentRepository deptRepo;
	@Autowired private DesignationRepository desigRepo;
	@Autowired private ThematicAreaRepository thematicAreaRepo;
	@Autowired private UserRepository userRepo;
	
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
	
	public ThematicAreaItem addThematicArea(AddThematicAreaRequest body) {
		ThematicArea ta = new ThematicArea();
		ta.setName(body.getName());
		ta.setEnabled(body.isEnabled());
		
		if(body.getProcessOwnerId() != null) {
			ta.setProcessOwner(userRepo.getOne(body.getProcessOwnerId()));
		}
		
		ta = thematicAreaRepo.save(ta);
		
		ThematicAreaItem dto = new ThematicAreaItem();
		
		dto.setId(ta.getId());
		dto.setEnabled(ta.isEnabled());
		dto.setName(ta.getName());
		dto.setProcessOwner(new UserLookupItem(ta.getProcessOwner().getId(), ta.getProcessOwner().getFullName()));
		
		return dto;
	}
	
	public List<ThematicAreaItem> getAllThematicAreas() {
		List<ThematicArea> tas = thematicAreaRepo.findAll();
		
		List<ThematicAreaItem> dtos =  tas.stream().map(a -> new ThematicAreaItem(a.getId(), a.getName(),
				a.getProcessOwner() != null ?
					new UserLookupItem(a.getProcessOwner().getId(), a.getProcessOwner().getFullName())
				: null,
				a.isEnabled())
				).collect(Collectors.toList());
		
		return dtos;
	}
	
	public void udpateThematicArea(UUID id, AddThematicAreaRequest body) {
		ThematicArea ta = thematicAreaRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid ID"));
		
		ta.setEnabled(body.isEnabled());
		ta.setName(body.getName());
		
		if(body.getProcessOwnerId() != null) {
			ta.setProcessOwner(userRepo.getOne(body.getProcessOwnerId()));
		}
		
		thematicAreaRepo.save(ta);
	}
}
