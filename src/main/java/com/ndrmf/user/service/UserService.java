package com.ndrmf.user.service;

import com.ndrmf.engine.dto.FipThematicAreasListItem;
import com.ndrmf.engine.model.ProjectProposalGeneralCommentModel;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.dto.ThematicAreaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.model.AccreditationQuestionairre;
import com.ndrmf.engine.model.FIPThematicArea;
import com.ndrmf.engine.repository.AccreditationQuestionairreRepository;
import com.ndrmf.engine.repository.FIPThematicAreaRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.model.ProcessType;
import com.ndrmf.setting.repository.DepartmentRepository;
import com.ndrmf.setting.repository.DesignationRepository;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.ThematicAreaRepository;
import com.ndrmf.user.dto.CreateUserRequest;
import com.ndrmf.user.dto.DefineUserThematicAreasRequest;
import com.ndrmf.user.dto.OrganisationAndRoles;
import com.ndrmf.user.dto.RoleItem;
import com.ndrmf.user.dto.SignupRequest;
import com.ndrmf.user.dto.SignupRequestItem;
import com.ndrmf.user.dto.UpdateProfileRequest;
import com.ndrmf.user.dto.UpdateUserRequest;
import com.ndrmf.user.dto.UserItem;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.dto.CreateTestTempRequest;
import com.ndrmf.user.model.Organisation;
import com.ndrmf.user.model.Role;
import com.ndrmf.user.model.Signup;
import com.ndrmf.user.model.User;
import com.ndrmf.user.model.TestTemp;
import com.ndrmf.user.repository.OrganisationRepository;
import com.ndrmf.user.repository.RoleRepository;
import com.ndrmf.user.repository.SignupRepository;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.repository.TempTestRepository;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.SignupRequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
	@Autowired private FIPThematicAreaRepository fipThematicAreaRepo;
	@Autowired private ThematicAreaRepository thematicAreaRepo;
	@Autowired private TempTestRepository tempTestRepo;
	@Autowired private NotificationService notificationService;
	
	@Transactional
    public String createUser(CreateUserRequest body) {
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

			try {
				notificationService.sendPlainTextEmail(
						process.getOwner().getEmail(),
						process.getOwner().getFullName(),
						"New Govt. User Created at NDRMF",
						"New Govt. user created. Please review and respond the request.\n" +
						"Please visit http://ndrmfdev.herokuapp.com/govt-accredit-requests\n" +
						"to fill the questionnaire."
				);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
        	
        	return "FIP_GOVT type user created successfully";
        }
        else {
        	return "FIP type user created successfully";
        }
    }
	
	@Transactional
	public void defineUserThematicAreas(UUID userId, DefineUserThematicAreasRequest body, String type) {
		Set<FIPThematicArea> areas = new HashSet<>();
//		try {
//			generalComments = objectMapper.readValue(generalCommentsJSON, objectMapper.getTypeFactory()
//					.constructCollectionType(List.class, ProjectProposalGeneralCommentModel.class));
//		} catch (Exception e) {
//			throw new RuntimeException("General Comments are not null but couldn't read it as JSON", e);
//		}
		if (type.equals("eligibility")){
			if(body.getAreas() != null) {
				body.getAreas().forEach(a -> {
					FIPThematicArea area = new FIPThematicArea();
					area.setFip(userRepo.getOne(userId));
					area.setThematicArea(thematicAreaRepo.getOne(a.getAreaId()));
					area.setExperience(a.getExperience());
					area.setCounterpart(a.getCounterpart());
					areas.add(area);
				});
			}
			fipThematicAreaRepo.saveAll(areas);
		}else{
			userRepo.findById(userId).get().setAvailableAsJv(body.isAvailableAsJv());
			userRepo.findById(userId).get().setJvUser(body.getJvUser());
		}
	}

	@Transactional
	public void defineUserThematicAreasByPo(UUID userId, DefineUserThematicAreasRequest body, String type) {
		Set<FIPThematicArea> areas = new HashSet<>();

		if (type.equals("eligibility")){
			if(body.getAreas() != null) {
				body.getAreas().forEach(a -> {
					FIPThematicArea area = new FIPThematicArea();
					area.setFip(userRepo.getOne(userId));
					area.setThematicArea(thematicAreaRepo.getOne(a.getAreaId()));
					area.setExperience(a.getExperience());
					area.setCounterpart(a.getCounterpart());
					areas.add(area);
				});
			}
			fipThematicAreaRepo.saveAll(areas);
		}else{
			userRepo.findById(userId).get().setAvailableAsJv(body.isAvailableAsJv());
			userRepo.findById(userId).get().setJvUser(body.getJvUser());
		}
	}

	@Transactional
	public void createTestTemp(CreateTestTempRequest body) {
		TestTemp tT = new TestTemp();
		tT.setName(body.getName());
		tT.setOrgId(body.getOrgId());
		tT.setMaritalStatus(body.getMaritalStatus());
		tT = tempTestRepo.save(tT);
	}
	
	public UserItem getUserById(UUID id) {
		User u = userRepo.findById(id).orElseThrow(() -> new ValidationException("Invalid User ID"));
		
		UserItem dto = new UserItem();
		
		dto.setId(u.getId());
		dto.setUsername(u.getUsername());
		dto.setEmail(u.getEmail());
		dto.setFirstName(u.getFirstName());
		dto.setLastName(u.getLastName());
		dto.setEnabled(u.isEnabled());
		dto.setSAP(u.isSAP());
		dto.setDepartmentId(u.getDepartment().getId());
		dto.setDesignationId(u.getDesignation().getId());
		
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
        	u.replaceRole(roleRepository.getOne(body.getRoleId()));
        }
        
        if(body.getDepartmentId() != null && body.getDepartmentId() > 0) {
        	u.setDepartment(deptRepo.getOne(body.getDepartmentId()));
        }
        
        if(body.getDesignationId() != null && body.getDesignationId() > 0) {
        	u.setDesignation(desigRepo.getOne(body.getDesignationId()));
        }
	}
	
	@Transactional
	public void updateProfile(UUID id, UpdateProfileRequest body) {
		User u = userRepo.findById(id).get();
		
		u.setFirstName(body.getFirstName());
		u.setLastName(body.getLastName());
		u.setPassword(passwordEncoder.encode(body.getPassword()));
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

		signup.setEntityName(body.getEntityName());
		signup.setEntityNature(body.getEntityNature());
		signup.setEntityType(body.getEntityType());
		signup.setLocation(body.getLocation());
		signup.setProvince(body.getProvince());
		signup.setAddress(body.getAddress());
		signup.setOtherAddress(body.getOtherAddress());
		signup.setOtherAccreditation(body.getOtherAccreditation());

		signup.setPassword(this.passwordEncoder.encode(body.getPassword()));
		
		signup.setApprovalStatus(SignupRequestStatus.PENDING.toString());
		
		signupRepo.save(signup);

		try {
			notificationService.sendPlainTextEmail(
					body.getEmail(),
					body.getFirstName(),
					"Signup at NDRMF",
					"Your sign up request has been initiated. We will review your application and once its approved " +
							"or Rejected we will inform you. Thanks!"
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		try {
			notificationService.sendPlainTextEmail(
					userRepo.findActiveUsersForRole(SystemRoles.SIGNUP_APPROVER).get(0).getEmail(),
					userRepo.findActiveUsersForRole(SystemRoles.SIGNUP_APPROVER).get(0).getFullName(),
					"New Sign up request at NDRMF",
					body.getFirstName() + " has submitted a new signup request. Please review and respond the request."
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}



	}
	
	public List<SignupRequestItem> getPendingSignupRequests() {
		List<Signup> reqs = signupRepo.findRequestsForStatus(SignupRequestStatus.PENDING.toString());
		
		List<SignupRequestItem> dtos = reqs.stream().map(req -> new SignupRequestItem(
				req.getId().toString(),
				req.getFirstName(),
				req.getLastName(),
				req.getEmail(),
				req.getCreatedDate(),
				req.getEntityName(),
				req.getEntityNature(),
				req.getEntityType(),
				req.getLocation(),
				req.getProvince(),
				req.getAddress(),
				req.getOtherAddress(),
				req.getOtherAccreditation()))
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
			dto.setEnabled(u.isEnabled());
			
			dto.setSAP(u.isSAP());
			
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
			dto.setEnabled(u.isEnabled());
			
			dto.setSAP(u.isSAP());
			
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
			
			dto.setSAP(u.isSAP());
			
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

	public List<UserItem> getAvailableAsJvUsers(UUID userId) {
		List<User> users = userRepo.findAllAvailableAsJvUsers(userId);

		List<UserItem> dtos = new ArrayList<>();

		users.forEach(u -> {
			UserItem dto = new UserItem();
			dto.setId(u.getId());
			dto.setUsername(u.getUsername());
			dto.setEmail(u.getEmail());
			dto.setFirstName(u.getFirstName());
			dto.setLastName(u.getLastName());
			dto.setEnabled(u.isEnabled());

			dto.setSAP(u.isSAP());

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

		u.setEntityName(s.getEntityName());
		u.setEntityNature(s.getEntityNature());
		u.setEntityType(s.getEntityType());
		u.setLocation(s.getLocation());
		u.setProvince(s.getProvince());
		u.setAddress(s.getAddress());
		u.setOtherAddress(s.getOtherAddress());
		u.setOtherAccreditation(s.getOtherAccreditation());

		u.setOrg(orgRepo.findByName(SystemRoles.ORG_FIP).get());
		u.addRole(roleRepository.findByName(SystemRoles.FIP_DATAENTRY));
		
		userRepo.save(u);

		try {
			notificationService.sendPlainTextEmail(
					s.getEmail(),
					s.getFirstName(),
					"Account at NDRMF has been approved;",
					s.getFirstName() + " your account has been approved at NDRMF. Kindly login to the portal at " +
							"http://ndrmfdev.herokuapp.com/"
			);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

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

	public List<FipThematicAreasListItem> getUserThematicAreas(
			UUID userId
	){
		List<FIPThematicArea> fiptalist = fipThematicAreaRepo.getAllThematicAreasForUser(userId);

		List<FipThematicAreasListItem> dto = new ArrayList<>();

		fiptalist.forEach(item -> {
			FipThematicAreasListItem fiptalitem = new FipThematicAreasListItem();
			fiptalitem.setId(item.getId());
			fiptalitem.setFip(new UserLookupItem(item.getFip().getId(), item.getFip().getFullName()));

			ThematicAreaItem tai = new ThematicAreaItem();
			tai.setId(item.getThematicArea().getId());
			tai.setName(item.getThematicArea().getName());
			tai.setProcessOwner(
				new UserLookupItem(
					item.getThematicArea().getProcessOwner().getId(),
					item.getThematicArea().getProcessOwner().getFullName()
				)
			);
			fiptalitem.setThematicAreaItem(tai);
			fiptalitem.setExperience(item.getExperience());
			fiptalitem.setCounterpart(item.getCounterpart());

			dto.add(fiptalitem);
		});


		return dto;
	}
}
