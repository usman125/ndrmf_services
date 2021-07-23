package com.ndrmf.common;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Lob;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "files")
public class rFile {
	private UUID id;
	private String fileName;
	private String fileType;
	private String path;
	private Date uploadedAt;
	private User uploadedBy;
	
	@Lob
	private byte[] data;
	
	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Column(nullable = false)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(nullable = false)
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
	
	@ManyToOne
	@JoinColumn(name = "uploader_user_id", nullable = false)
	public User getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
