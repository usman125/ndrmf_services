package com.ndrmf.engine.model;


import com.ndrmf.common.rFile;
import com.ndrmf.config.audit.Auditable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "grant_disbursment_withdrawal_files")
public class GrantDisbursmentWithdrawalFiles extends Auditable<String> {
	private long id;
	private InitialAdvance iaRef;
	private QuarterAdvance qaRef;
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
	@JoinColumn(name = "ia_ref")
	public InitialAdvance getIaRef() {
		return iaRef;
	}
	public void setIaRef(InitialAdvance iaRef) {
		this.iaRef = iaRef;
	}

	@ManyToOne
	@JoinColumn(name = "qa_ref")
	public QuarterAdvance getQaRef() {
		return qaRef;
	}
	public void setQaRef(QuarterAdvance qaRef) {
		this.qaRef = qaRef;
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
