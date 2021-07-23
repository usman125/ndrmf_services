package com.ndrmf.complaint.dto;

import java.util.Date;
import java.util.UUID;

public class ListComplaintAttachment {

	private UUID complaintId;
	private String fileName;
	private String path;
	private Date uploadedAt;
	
	public ListComplaintAttachment() {}
	
	public ListComplaintAttachment(UUID complaintId, String fileName, String path, Date uploadedAt) {
		super();
		this.complaintId = complaintId;
		this.fileName = fileName;
		this.path = path;
		this.uploadedAt = uploadedAt;
	}
	public UUID getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(UUID complaintId) {
		this.complaintId = complaintId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Date getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
	
	
}
