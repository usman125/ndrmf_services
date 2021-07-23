package com.ndrmf.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SignupRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String password;

	private String entityName;
	private String entityNature;
	private String entityType;
	private String location;
	private String province;
	private String address;
	private String otherAddress;
	private String otherAccreditation;
	
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
	
	@NotBlank
	@Email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotBlank
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public void setEntityNature(String entityNature) {
		this.entityNature = entityNature;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
}
