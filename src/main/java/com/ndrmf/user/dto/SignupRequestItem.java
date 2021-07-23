package com.ndrmf.user.dto;

import java.util.Date;

public class SignupRequestItem {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private Date createdAt;

	private String entityName;
	private String entityNature;
	private String entityType;
	private String location;
	private String province;
	private String address;
	private String otherAddress;
	private String otherAccreditation;
	
	public SignupRequestItem(
		String id,
		String firstName,
		String lastName,
		String email,
		Date createdAt,
		String entityName,
		String entityNature,
		String entityType,
		String location,
		String province,
		String address,
		String otherAddress,
		String otherAccreditation
	) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.createdAt = createdAt;
		this.entityName = entityName;
		this.entityNature = entityNature;
		this.entityType = entityType;
		this.location = location;
		this.province = province;
		this.address = address;
		this.otherAddress = otherAddress;
		this.otherAccreditation = otherAccreditation;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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
}
