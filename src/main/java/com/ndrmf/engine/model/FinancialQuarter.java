package com.ndrmf.engine.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ndrmf.user.model.User;

@Entity
@Table(name = "financial_quarter")
public class FinancialQuarter {

	private UUID Id;
	private GrantDisbursment disbursmentRef;
	private User FMG;
	private User PAM;
	private User MnE;
	private User proc;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "disbursment_id", nullable = false)
	public GrantDisbursment getDisbursmentRef() {
		return disbursmentRef;
	}
	public void setDisbursmentRef(GrantDisbursment disbursmentRef) {
		this.disbursmentRef = disbursmentRef;
	}
	
	@ManyToOne
	@JoinColumn(name = "fmg_user_id", nullable = false)
	public User getFMG() {
		return FMG;
	}
	public void setFMGe(User FMG) {
		this.FMG = FMG;
	}
	
	@ManyToOne
	@JoinColumn(name = "pam_user_id", nullable = false)
	public User getPAM() {
		return PAM;
	}
	public void setPAM(User pAM) {
		PAM = pAM;
	}
	
	@ManyToOne
	@JoinColumn(name = "mne_user_id", nullable = false)
	public User getMnE() {
		return MnE;
	}
	public void setMnE(User mnE) {
		MnE = mnE;
	}
	
	@ManyToOne
	@JoinColumn(name = "proc_user_id", nullable = false)
	public User getProc() {
		return proc;
	}
	public void setProc(User proc) {
		this.proc = proc;
	}
	public void setFMG(User fMG) {
		FMG = fMG;
	}
	
}
