package com.ndrmf.user.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserItem {
	private UUID id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private int orgId;
	private String orgName;
	private List<Map<String, Object>> roles;
	private boolean enabled;
	private boolean isSAP;
	private boolean availableAsJv;

	private int departmentId;
	private int designationId;

	private String entityName;
	private String entityNature;
	private String entityType;
	private String location;
	private String province;
	private String address;
	private String otherAddress;
	private String otherAccreditation;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public List<Map<String, Object>> getRoles() {
		return roles;
	}
	public void setRoles(List<Map<String, Object>> roles) {
		this.roles = roles;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public boolean isSAP() {
		return isSAP;
	}
	public void setSAP(boolean isSAP) {
		this.isSAP = isSAP;
	}
	public boolean isAvailableAsJv() {
		return availableAsJv;
	}
	public void setAvailableAsJv(boolean availableAsJv) {
		this.availableAsJv = availableAsJv;
	}


	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityNature() {
		return entityNature;
	}

	public void setEntityNature(String entityNature) {
		this.entityNature = entityNature;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAccreditation(String otherAccreditation) {
		this.otherAccreditation = otherAccreditation;
	}

	public String getOtherAccreditation() {
		return otherAccreditation;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public int getDesignationId() {
		return designationId;
	}

	public void setDesignationId(int designationId) {
		this.designationId = designationId;
	}
}
