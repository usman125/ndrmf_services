package com.ndrmf.engine.model;


import com.ndrmf.common.rFile;
import com.ndrmf.config.audit.Auditable;

import javax.persistence.*;

@Entity
@Table(name = "tpv_tasks_files")
public class TpvTasksFiles extends Auditable<String> {
	private long id;
	private InitialAdvance iaRef;
	private TpvTasks tpvTaskRef;
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
	@JoinColumn(name = "tpvTask_ref")
	public TpvTasks getTpvTaskRef() {
		return tpvTaskRef;
	}
	public void setTpvTaskRef(TpvTasks tpvTaskRef) {
		this.tpvTaskRef = tpvTaskRef;
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
