package com.ndrmf.user.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ndrmf.config.audit.Auditable;

@Entity
@Table(name = "signup_requests")
public class Signup extends Auditable<String> {
	private UUID id;
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

	private String approvalStatus;
	private String approvalRemarks;
	
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
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
	
	@Column(unique = true, nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
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
