package com.ndrmf.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndrmf.exception.ValidationException;
import com.ndrmf.request.*;
import com.ndrmf.response.ServiceResponse;
import com.ndrmf.setting.repository.DepartmentRepository;
import com.ndrmf.setting.repository.DesignationRepository;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;
import com.ndrmf.user.dto.UserItem;
import com.ndrmf.user.model.Organisation;
import com.ndrmf.user.model.Role;
import com.ndrmf.user.model.Signup;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.OrganisationRepository;
import com.ndrmf.user.repository.RoleRepository;
import com.ndrmf.user.repository.SignupRepository;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.CommonConstants;
import com.ndrmf.util.CommonUtils;
import com.ndrmf.util.enums.SignupRequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        
        userRepo.save(u);
    }

    public ResponseEntity<ServiceResponse> addRolesForUser(AddRoleUserRequest addRoleUserRequest) {
        ResponseEntity<ServiceResponse> addRoleUserResponseEntity;
        ServiceResponse serviceResponse;

        if (CommonUtils.isInvalidUserRoleRequest(addRoleUserRequest)) {
            serviceResponse = CommonUtils.invalidClientReqResponse();
            addRoleUserResponseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Role role = roleRepository.findByName(addRoleUserRequest.getName());
            User user = userRepo.findByUsername(addRoleUserRequest.getUsername());

            if (null == role || null == user) {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                addRoleUserResponseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                //role.getUserSet().add(user);
                User updatedUser = userRepo.save(user);
                serviceResponse = CommonUtils.mapAddRoleUserResponse(updatedUser);
                addRoleUserResponseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return addRoleUserResponseEntity;
    }
    
    public ResponseEntity<ServiceResponse> updateProfile(UserActivationRequest userActivationRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if (CommonUtils.isInvalidProfileUpdate(userActivationRequest)) {

            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            User user = userRepo.findByUsername(userActivationRequest.getUsername());
            if (null == user) {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                user.setEnabled(userActivationRequest.isActive());
                userRepo.save(user);
                serviceResponse = CommonUtils.successResponse(CommonConstants.UPDATE_USER_PROFILE_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return responseEntity;
    }
    
    public ResponseEntity<ServiceResponse> updateProfile(UserEligibilityRequest userEligibilityRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if (CommonUtils.isInvalidProfileUpdate(userEligibilityRequest)) {

            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            User user = userRepo.findByUsername(userEligibilityRequest.getUsername());
            if (null == user) {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                userRepo.save(user);
                serviceResponse = CommonUtils.successResponse(CommonConstants.UPDATE_USER_PROFILE_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return responseEntity;
    }
    
    public ResponseEntity<ServiceResponse> updateProfile(UserQualificationRequest userQualificationRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if (CommonUtils.isInvalidProfileUpdate(userQualificationRequest)) {

            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            User user = userRepo.findByUsername(userQualificationRequest.getUsername());
            if (null == user) {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceResponse);
            } else {
                userRepo.save(user);
                serviceResponse = CommonUtils.successResponse(CommonConstants.UPDATE_USER_PROFILE_SUCCESS_DESC);
                responseEntity = ResponseEntity.ok(serviceResponse);
            }
        }

        return responseEntity;
    }
    
    public ResponseEntity<ServiceResponse> getUsers(String fetchOption) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;
        List<User> users = null;

        if (CommonConstants.FETCH_ALL_USER_OPTION.equals(fetchOption)) {
            users = userRepo.findAll();
        } else if (CommonConstants.FETCH_ACTIVE_USER_OPTION.equals(fetchOption)) {
            users = userRepo.findAllByEnabledTrue();
        } else if (CommonConstants.FETCH_INACTIVE_USER_OPTION.equals(fetchOption)) {
            users = userRepo.findAllByEnabledFalse();
        }

        if (CommonUtils.isNullOrEmptyCollection(users)) {
            serviceResponse = CommonUtils.dataNotFoundResponse(null);
        } else {
            serviceResponse = CommonUtils.mapGetUsers(users);
        }
        responseEntity = ResponseEntity.ok(serviceResponse);

        return responseEntity;
    }
    
    public ResponseEntity<ServiceResponse> getUsersHavingRole(String roleName) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse;

        if (null == roleName) {
            serviceResponse = CommonUtils.invalidClientReqResponse();
            responseEntity = ResponseEntity.badRequest().body(serviceResponse);
        } else {
            Role role = roleRepository.findByName(roleName);
            if (null == role) {
                serviceResponse = CommonUtils.dataNotFoundResponse(null);
            } else {
                List<User> users = userRepo.findAll();
                if (CommonUtils.isNullOrEmptyCollection(users)) {
                    serviceResponse = CommonUtils.dataNotFoundResponse(null);
                } else {
                    serviceResponse = CommonUtils.mapGetUsers(users);
                }
            }
            responseEntity = ResponseEntity.ok(serviceResponse);
        }

        return responseEntity;
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
	
	public List<UserItem> getAllUsers() {
		List<User> users = userRepo.findAll();
		
		List<UserItem> dtos = new ArrayList<>();
		
		users.forEach(u -> {
			UserItem dto = new UserItem();
			
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
		
		if(s.getApprovalStatus() != SignupRequestStatus.PENDING.toString()) {
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
		u.setOrg(orgRepo.findByName("FIP").get());
		
		userRepo.save(u);
	}
	
	public void getUsersForRole(String roleName) {
		List<User> users = userRepo.findUsersForRole(roleName);
		
		
	}
}
