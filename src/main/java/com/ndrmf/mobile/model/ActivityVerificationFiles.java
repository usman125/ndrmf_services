package com.ndrmf.mobile.model;


import com.ndrmf.common.rFile;
import com.ndrmf.config.audit.Auditable;
import com.ndrmf.engine.model.InitialAdvance;
import com.ndrmf.engine.model.QuarterAdvance;

import javax.persistence.*;

@Entity
@Table(name = "activity_verification_files")
public class ActivityVerificationFiles extends Auditable<String> {
	private long id;
	private ActivityVerification avRef;
	private rFile fileRef;
	private byte[] picByte;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "av_ref")
	public ActivityVerification getAvRef() {
		return avRef;
	}
	public void setAvRef(ActivityVerification avRef) {
		this.avRef = avRef;
	}

	@ManyToOne
	@JoinColumn(name = "file_ref")
	public rFile getFileRef() {
		return fileRef;
	}
	public void setFileRef(rFile fileRef) {
		this.fileRef = fileRef;
	}


	public byte[] getPicByte() {
		return picByte;
	}
	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

}
