package com.ndrmf.engine.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import com.ndrmf.util.enums.ProcessStatus;


@Entity
@Table(name = "grant_disbursment")
public class GrantDisbursment extends Auditable<String>{
	//
	//private ProjectProposal projectProposalRef;
	/*tasks:
	 * 1. PAM invite to FIP to apply for init Advance after GIA
	 * 2. FIP apply for init advance
	 */
	
	private UUID id;
	private ProjectProposal proposalRef;
	private String data;
	private String status;
	private String subStatus;
	private User owner;
	private ProcessStatus initAdvanceRequestStatus;
	private InitialAdvance initAdvance;
	private ProcessStatus nextQuarterRequestStatus;
	private List<FinancialQuarter> quarters;
	private List<QuarterAdvance> quarterAdvances;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	@OneToOne(mappedBy="grantDisbursment")
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}
	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	public ProcessStatus getInitAdvanceRequestStatus() {
		return initAdvanceRequestStatus;
	}
	public void setInitAdvanceRequestStatus(ProcessStatus initAdvanceRequestStatus) {
		this.initAdvanceRequestStatus = initAdvanceRequestStatus;
	}
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "init_advance_id")
	public InitialAdvance getInitAdvance() {
		return initAdvance;
	}
	public void setInitAdvance(InitialAdvance initAdvance) {
		this.initAdvance = initAdvance;
	}
	public ProcessStatus getNextQuarterRequestStatus() {
		return nextQuarterRequestStatus;
	}
	public void setNextQuarterRequestStatus(ProcessStatus nextQuarterRequestStatus) {
		this.nextQuarterRequestStatus = nextQuarterRequestStatus;
	}
	@OneToMany(mappedBy="disbursmentRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<FinancialQuarter> getQuarters() {
		return quarters;
	}
	public void setQuarters(List<FinancialQuarter> quarters) {
		this.quarters = quarters;
	}

	@OneToMany(mappedBy = "grantDisbursmentRef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<QuarterAdvance> getQuarterAdvances() {
		return quarterAdvances;
	}
	public void setQuarterAdvances(List<QuarterAdvance> quarterAdvances) {
		this.quarterAdvances = quarterAdvances;
	}

	public void addQuarterAdvance(QuarterAdvance advance) {
		if(this.quarterAdvances == null) {
			this.quarterAdvances = new ArrayList<>();
		}

		advance.setGrantDisbursmentRef(this);
		this.quarterAdvances.add(advance);
	}

	@ManyToOne
	@JoinColumn(name = "owner_user_id")
	public User getOwner() { return owner; }
	public void setOwner(User owner) { this.owner = owner;}
}
