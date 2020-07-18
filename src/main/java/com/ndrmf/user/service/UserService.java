package com.ndrmf.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.model.AccreditationQuestionairre;
import com.ndrmf.engine.repository.AccreditationQuestionairreRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.model.ProcessType;
import com.ndrmf.setting.repository.DepartmentRepository;
import com.ndrmf.setting.repository.DesignationRepository;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.RoleItem;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;
import com.ndrmf.user.dto.UpdateUserRequest;
import com.ndrmf.user.dto.UserItem;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.Organisation;
import com.ndrmf.user.model.Role;
import com.ndrmf.user.model.Signup;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.OrganisationRepository;
import com.ndrmf.user.repository.RoleRepository;
import com.ndrmf.user.repository.SignupRepository;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.SignupRequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class UserService {
	@Autowired private UserRepository userRepo;
	@Autowired private RoleRepository roleRepository;
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	@Autowired private OrganisationRepository orgRepo;
	@Autowired private DepartmentRepository deptRepo;
	@Autowired private DesignationRepository desigRepo;
	@Autowired private SignupRepository signupRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private AccreditationQuestionairreRepository questionairreRepo;
	
	@Transactional
    public void createUser(CreateUserRequest body) {
        User u = new User();
        u.setEmail(body.getEmail());
        u.setEnabled(true);
        u.setFirstName(body.getFirstName());
        u.setLastName(body.getLastName());
        u.setOrg(orgRepo.getOne(body.getOrgId()));
        u.setUsername(body.getUsername());
        u.setPassword(passwordEncoder.encode(body.getPassword()));
        
        if(body.getRoleId() != null && body.getRoleId() > 0) {
        	u.addRole(roleRepository.getOne(body.getRoleId()));
        }
        
        if(body.getDepartmentId() != null && body.getDepartmentId() > 0) {
        	u.setDepartment(deptRepo.getOne(body.getDepartmentId()));
        }
        
        if(body.getDesignationId() != null && body.getDesignationId() > 0) {
        	u.setDesignation(desigRepo.getOne(body.getDesignationId()));
        }
        
        u = userRepo.save(u);
        
        if(body.getOrgId() == SystemRoles.ORG_GOVT_ID && body.getRoleId() == SystemRoles.FIP_GOVT_ID) {
        	ProcessType process = processTypeRepo.findById(com.ndrmf.util.enums.ProcessType.ACCREDITATION_QUESTIONNAIRE.name())
        			.orElseThrow(() -> new RuntimeException("ACCREDITATION_QUESTIONNAIRE must be defined before creating GOVT FIP user"));
        	
        	if(process.getOwner() == null) {
        		throw new ValidationException("Define Process Owner for process ACCREDITATION_QUESTIONNAIRE to create GOVT FIP user");
        	}
        	
        	AccreditationQuestionairre q = new AccreditationQuestionairre();
        	q.setAssignee(process.getOwner());
        	q.setForUser(u);
        	q.setStatus(ProcessStatus.PENDING.getPersistenceValue());
        	
        	questionairreRepo.save(q);
        }
    }
	
	public UserItem getUserById(UUID id) {
		User u = userRepo.findById(id).orElseThrow(() -> new ValidationException("Invalid User ID"));
		
		UserItem dto = new UserItem();
		
		dto.setId(u.getId());
		dto.setUsername(u.getUsername());
		dto.setEmail(u.getEmail());
		dto.setFirstName(u.getFirstName());
		dto.setLastName(u.getLastName());
		dto.setEnabled(false);
		
		if(u.getOrg() != null) {
			dto.setOrgId(u.getOrg().getId());
			dto.setOrgName(u.getOrg().getName());
		}
		
		List<Map<String, Object>> roles = new ArrayList<>();
		
		if(u.getRoles() != null) {
			u.getRoles().forEach(r -> {
				Map<String, Object> role = new HashMap<>();
				role.put("id", r.getId());
				role.put("name", r.getName());
				
				roles.add(role);
			});
			
			dto.setRoles(roles);
		}
		
		return dto;
	}
	
	@Transactional
	public void updateUser(UUID id, UpdateUserRequest body) {
		User u = userRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid User ID"));
		
		if(body.isEnabled() != null)
			u.setEnabled(body.isEnabled());
		
		if(body.getFirstName() != null)
			u.setFirstName(body.getFirstName());
		
		if(body.getLastName() != null)
			u.setLastName(body.getLastName());
		
		if(body.getOrgId() != null)
			u.setOrg(orgRepo.getOne(body.getOrgId()));
		
		if(body.getPassword() != null)
			u.setPassword(passwordEncoder.encode(body.getPassword()));
        
        if(body.getRoleId() != null && body.getRoleId() > 0) {
        	u.addRole(roleRepository.getOne(body.getRoleId()));
        }
        
        if(body.getDepartmentId() != null && body.getDepartmentId() > 0) {
        	u.setDepartment(deptRepo.getOne(body.getDepartmentId()));
        }
        
        if(body.getDesignationId() != null && body.getDesignationId() > 0) {
        	u.setDesignation(desigRepo.getOne(body.getDesignationId()));
        }
	}
    
	public List<OrganisationAndRoles> getOrganisations() {
		List<Organisation> orgs = orgRepo.findAll();
		
		List<OrganisationAndRoles> dtos = new ArrayList<>();
		
		orgs.forEach(org -> {
			OrganisationAndRoles dto = new OrganisationAndRoles();
			dto.setId(org.getId());
			dto.setName(org.getName());
			
			if(org.getRoles() != null) {
				org.getRoles().forEach(r -> {
					dto.addRole(r.getId(), r.getName());
				});
			}
			
			dtos.add(dto);
			
		}); 
		
		return dtos;
	}
	
	public void createSignup(SignupRequest body) {
		Signup signup = new Signup();
		
		signup.setFirstName(body.getFirstName());
		signup.setLastName(body.getLastName());
		signup.setEmail(body.getEmail());
		signup.setPassword(this.passwordEncoder.encode(body.getPassword()));
		
		signup.setApprovalStatus(SignupRequestStatus.PENDING.toString());
		
		signupRepo.save(signup);
	}
	
	public List<SignupRequestItem> getPendingSignupRequests() {
		List<Signup> reqs = signupRepo.findRequestsForStatus(SignupRequestStatus.PENDING.toString());
		
		List<SignupRequestItem> dtos = reqs.stream().map(req -> new SignupRequestItem(req.getId().toString(), req.getFirstName(), req.getLastName(), req.getEmail(), req.getCreatedDate()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	public List<UserItem> getActiveUsers() {
		List<User> users = userRepo.findAllByEnabledTrue();
		
		List<UserItem> dtos = new ArrayList<>();
		
		users.forEach(u -> {
			UserItem dto = new UserItem();
			
			dto.setId(u.getId());
			dto.setUsername(u.getUsername());
			dto.setEmail(u.getEmail());
			dto.setFirstName(u.getFirstName());
			dto.setLastName(u.getLastName());
			dto.setEnabled(false);
			
			if(u.getOrg() != null) {
				dto.setOrgId(u.getOrg().getId());
				dto.setOrgName(u.getOrg().getName());
			}
			
			List<Map<String, Object>> roles = new ArrayList<>();
			
			if(u.getRoles() != null) {
				u.getRoles().forEach(r -> {
					Map<String, Object> role = new HashMap<>();
					role.put("id", r.getId());
					role.put("name", r.getName());
					
					roles.add(role);
				});
				
				dto.setRoles(roles);
			}
			
			dtos.add(dto);
			
		});
		
		return dtos;
	}
	
	public List<UserItem> getUsersWithMissigCredentials() {
		List<User> users = userRepo.findAllUsersWithPasswordNull();
		
		List<UserItem> dtos = new ArrayList<>();
		
		users.forEach(u -> {
			UserItem dto = new UserItem();
			
			dto.setId(u.getId());
			dto.setUsername(u.getUsername());
			dto.setEmail(u.getEmail());
			dto.setFirstName(u.getFirstName());
			dto.setLastName(u.getLastName());
			dto.setEnabled(false);
			
			if(u.getOrg() != null) {
				dto.setOrgId(u.getOrg().getId());
				dto.setOrgName(u.getOrg().getName());
			}
			
			List<Map<String, Object>> roles = new ArrayList<>();
			
			if(u.getRoles() != null) {
				u.getRoles().forEach(r -> {
					Map<String, Object> role = new HashMap<>();
					role.put("id", r.getId());
					role.put("name", r.getName());
					
					roles.add(role);
				});
				
				dto.setRoles(roles);
			}
			
			dtos.add(dto);
			
		});
		
		return dtos;
	}
	
	public Map<String, List<UserLookupItem>> getActiveUsersGroupedByDepartment(){
		List<User> users = userRepo.findActiveUsersByOrganisation(SystemRoles.ORG_NDRMF_ID);
		users = users.stream().filter(u -> u.getDepartment() != null).collect(Collectors.toList());
		
		Map<String, List<User>> groupedUsers = users.stream().collect(Collectors.groupingBy(u -> u.getDepartment().getName()));
		
		Map<String, List<UserLookupItem>> dtos = new HashMap<>();
		
		groupedUsers.entrySet().forEach(es -> {
			List<UserLookupItem> usersInGroup = es.getValue().stream()
					.map(u -> new UserLookupItem(u.getId(), u.getFullName()))
					.collect(Collectors.toList());
			
			dtos.put(es.getKey(), usersInGroup);
		});
		
		return dtos;
	}
	
	public List<UserItem> getAllUsers() {
		List<User> users = userRepo.findAll();
		
		List<UserItem> dtos = new ArrayList<>();
		
		users.forEach(u -> {
			UserItem dto = new UserItem();
			dto.setId(u.getId());
			dto.setUsername(u.getUsername());
			dto.setEmail(u.getEmail());
			dto.setFirstName(u.getFirstName());
			dto.setLastName(u.getLastName());
			dto.setEnabled(u.isEnabled());
			
			if(u.getOrg() != null) {
				dto.setOrgId(u.getOrg().getId());
				dto.setOrgName(u.getOrg().getName());
			}
			
			List<Map<String, Object>> roles = new ArrayList<>();
			
			if(u.getRoles() != null) {
				u.getRoles().forEach(r -> {
					Map<String, Object> role = new HashMap<>();
					role.put("id", r.getId());
					role.put("name", r.getName());
					
					roles.add(role);
				});
				
				dto.setRoles(roles);
			}
			
			dtos.add(dto);
			
		});
		
		return dtos;
	}
	
	@Transactional
	public void approveSignupRequest(UUID id, String remarks) {
		Signup s = signupRepo.findById(id)
				.orElseThrow(() -> new ValidationException("No request found for ID: "+id.toString()));
		
		if(!s.getApprovalStatus().equalsIgnoreCase(SignupRequestStatus.PENDING.toString())) {
			throw new ValidationException("Cannot approve. Request is already: " + s.getApprovalStatus());
		}
		
		s.setApprovalRemarks(remarks);
		s.setApprovalStatus(SignupRequestStatus.APPROVED.toString());
		
		User u = new User();
		
		u.setUsername(s.getEmail());
		u.setEmail(s.getEmail());
		u.setEnabled(true);
		u.setFirstName(s.getFirstName());
		u.setLastName(s.getLastName());
		u.setPassword(s.getPassword());
		u.setOrg(orgRepo.findByName(SystemRoles.ORG_FIP).get());
		u.addRole(roleRepository.findByName(SystemRoles.FIP_DATAENTRY));
		
		userRepo.save(u);
	}
	
	public List<UserLookupItem> getActiveUsersForLookupByRole(String roleName) {
		List<User> users = userRepo.findActiveUsersForRole(roleName);
		
		List<UserLookupItem> dtos = users.stream()
				.map(u -> new UserLookupItem(u.getId(), u.getFullName()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	public List<RoleItem> getRolesForOrganisation(int orgId){
		List<Role> roles = roleRepository.findRolesByOrg(orgId);
		
		List<RoleItem> dtos = roles.stream().map(r -> new RoleItem(r.getId(), r.getName())).collect(Collectors.toList());
		
		return dtos;
	}
	
	public Optional<User> getDMPAM() {
		List<User> users = userRepo.findActiveUsersForRole(SystemRoles.DM_PAM);
		
		if(users == null || users.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(users.get(0));
	}
}
