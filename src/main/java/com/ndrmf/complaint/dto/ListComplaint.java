package com.ndrmf.complaint.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ListComplaint {

	private UUID id;
	private String complainantName;
	private String address;
	private String gender;
	private String contactNo;
	private String cnic;
	private String email;
	private String complaintAgainstPersonOrDepartment;
	private String organizationName;
	private String vendor;
	private String consultantName;
	private String complaintCategory;
	private String shortDescription;
	private String shortDescFectorCausingProblem;
	private LocalDateTime complaintDateTime;
	private String complainantSignature;
	private String complaintLocation;
	private String complaintSubLocation;
	private String status;
	private String internalStatus;
	private Long seqNo;
	private String priority;

	private List<ListComplaintAssignee> assignees;
	private List<ListComplaintReview> reviews;
	private List<ListComplainantReview> complainantReviews;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getComplainantName() {
		return complainantName;
	}

	public void setComplainantName(String complainantName) {
		this.complainantName = complainantName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComplaintAgainstPersonOrDepartment() {
		return complaintAgainstPersonOrDepartment;
	}

	public void setComplaintAgainstPersonOrDepartment(String complaintAgainstPersonOrDepartment) {
		this.complaintAgainstPersonOrDepartment = complaintAgainstPersonOrDepartment;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public String getComplaintCategory() {
		return complaintCategory;
	}

	public void setComplaintCategory(String complaintCategory) {
		this.complaintCategory = complaintCategory;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getShortDescFectorCausingProblem() {
		return shortDescFectorCausingProblem;
	}

	public void setShortDescFectorCausingProblem(String shortDescFectorCausingProblem) {
		this.shortDescFectorCausingProblem = shortDescFectorCausingProblem;
	}

	public LocalDateTime getComplaintDateTime() {
		return complaintDateTime;
	}

	public void setComplaintDateTime(LocalDateTime complaintDateTime) {
		this.complaintDateTime = complaintDateTime;
	}

	public String getComplainantSignature() {
		return complainantSignature;
	}

	public void setComplainantSignature(String complainantSignature) {
		this.complainantSignature = complainantSignature;
	}

	public String getComplaintLocation() {
		return complaintLocation;
	}

	public void setComplaintLocation(String complaintLocation) {
		this.complaintLocation = complaintLocation;
	}

	public String getComplaintSubLocation() {
		return complaintSubLocation;
	}

	public void setComplaintSubLocation(String complaintSubLocation) {
		this.complaintSubLocation = complaintSubLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInternalStatus() {
		return internalStatus;
	}

	public void setInternalStatus(String internalStatus) {
		this.internalStatus = internalStatus;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public List<ListComplaintAssignee> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<ListComplaintAssignee> assignees) {
		this.assignees = assignees;
	}

	public List<ListComplaintReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ListComplaintReview> reviewList) {
		this.reviews = reviewList;
	}

	public List<ListComplainantReview> getComplainantReviews() {
		return complainantReviews;
	}

	public void setComplainantReviews(List<ListComplainantReview> complainantReviews) {
		this.complainantReviews = complainantReviews;
	}


}
