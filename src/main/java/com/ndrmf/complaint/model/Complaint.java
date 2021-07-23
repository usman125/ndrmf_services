package com.ndrmf.complaint.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "complaint")
public class Complaint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 302920822715443511L;
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

	private List<ComplaintReview> reviews;
	private List<ComplaintAttachment> attachments;
	private List<ComplaintAssignee> assignee;
	

	public Complaint() {
	}

	public Complaint(UUID id, String complainantName, String address, String gender, String contactNo, String cnic,
			String email, String complaintAgainstPersonOrDepartment, String organizationName, String vendor,
			String consultantName, String complaintCategory, String shortDescription,
			String shortDescFectorCausingProblem, LocalDateTime complaintDateTime, String complainantSignature,
			String complaintLocation, String complaintSubLocation, String status, String internalStatus, Long seqNo,
			String priority, List<ComplaintReview> reviews, List<ComplaintAttachment> attachments,
			List<ComplaintAssignee> assignee) {
		super();
		this.id = id;
		this.complainantName = complainantName;
		this.address = address;
		this.gender = gender;
		this.contactNo = contactNo;
		this.cnic = cnic;
		this.email = email;
		this.complaintAgainstPersonOrDepartment = complaintAgainstPersonOrDepartment;
		this.organizationName = organizationName;
		this.vendor = vendor;
		this.consultantName = consultantName;
		this.complaintCategory = complaintCategory;
		this.shortDescription = shortDescription;
		this.shortDescFectorCausingProblem = shortDescFectorCausingProblem;
		this.complaintDateTime = complaintDateTime;
		this.complainantSignature = complainantSignature;
		this.complaintLocation = complaintLocation;
		this.complaintSubLocation = complaintSubLocation;
		this.status = status;
		this.internalStatus = internalStatus;
		this.seqNo = seqNo;
		this.priority = priority;
		this.reviews = reviews;
		this.attachments = attachments;
		this.assignee = assignee;
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

	@OneToMany(mappedBy = "complaintRef", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<ComplaintReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ComplaintReview> reviews) {
		this.reviews = reviews;
	}

	public void addComplaintReview(ComplaintReview r) {
		if (this.reviews == null) {
			this.reviews = new ArrayList<>();
		}

		r.setComplaintRef(this);

		this.reviews.add(r);
	}

	@OneToMany(mappedBy = "complaintRef", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<ComplaintAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ComplaintAttachment> attachments) {
		this.attachments = attachments;
	}

	public void addComplaintAttachement(ComplaintAttachment a) {
		if (this.attachments == null) {
			this.attachments = new ArrayList<>();
		}

		a.setComplaintRef(this);

		this.attachments.add(a);
	}

	@OneToMany(mappedBy = "complaintRef", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<ComplaintAssignee> getAssignee() {
		return assignee;
	}

	public void setAssignee(List<ComplaintAssignee> assignee) {
		this.assignee = assignee;
	}

	public void addComplaintAssignee(ComplaintAssignee e) {
		if (this.assignee == null) {
			this.assignee = new ArrayList<>();
		}
		e.setComplaintRef(this);
		this.assignee.add(e);
	}

	
}
