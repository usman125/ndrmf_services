package com.ndrmf.engine.model;


import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.Date;
import javax.persistence.*;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.util.enums.ProcessStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "initial_advance")
public class InitialAdvance extends Auditable<String> {

	private String payeesName;
	private String payeesAddress;
	private String bankName;
	private String bankAddress;
	private String payeesAccount;
	private String swiftCode;
	private String specialPaymentInstruction;

	private Integer quarter;

	private String reassignComments;
	private Date reassignedOn;

	@OneToMany(mappedBy="iaRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<GrantDisbursmentWithdrawalFiles> attachments;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	private UUID Id;


	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String data;

	private Integer amount;

	@OneToOne(mappedBy = "initAdvance")
	private GrantDisbursment disbursmentRef;

	private ProcessStatus initAdvanceStatus;
	private ProcessStatus status;
	private ProcessStatus subStatus;

	@OneToMany(mappedBy = "initialAdvanceRef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<GrantDisbursmentAdvanceReviews> reviewsList;

	@OneToMany(mappedBy = "initialAdvanceRef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<GrantDisbursmentAdvanceLiquidation> grantDisbursmentAdvanceLiquidations;


	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
	}
	public GrantDisbursment getDisbursmentRef() {
		return disbursmentRef;
	}
	public void setDisbursmentRef(GrantDisbursment disbursmentRef) {
		this.disbursmentRef = disbursmentRef;
	}
	public ProcessStatus getInitAdvanceStatus() {
		return initAdvanceStatus;
	}
	public void setInitAdvanceStatus(ProcessStatus initAdvanceStatus) {
		this.initAdvanceStatus = initAdvanceStatus;
	}
	public ProcessStatus getStatus() {
		return status;
	}
	public void setStatus(ProcessStatus status) {
		this.status = status;
	}

	public ProcessStatus getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(ProcessStatus subStatus) {
		this.subStatus = subStatus;
	}

	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return data;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getAmount() {
		return amount;
	}

	public void setReviewsList(List<GrantDisbursmentAdvanceReviews> reviewsList) {
		this.reviewsList = reviewsList;
	}

	public List<GrantDisbursmentAdvanceReviews> getReviewsList() {
		return reviewsList;
	}

	public void addReviewList(GrantDisbursmentAdvanceReviews review) {
		if(this.reviewsList == null) {
			this.reviewsList = new ArrayList<>();
		}

		review.setInitialAdvanceRef(this);
		this.reviewsList.add(review);
	}

//	public void setGrantDisbursmentAdvanceLiquidationRef(GrantDisbursmentAdvanceLiquidation grantDisbursmentAdvanceLiquidationRef) {
//		this.grantDisbursmentAdvanceLiquidationRef = grantDisbursmentAdvanceLiquidationRef;
//	}
//
//	public GrantDisbursmentAdvanceLiquidation getGrantDisbursmentAdvanceLiquidationRef() {
//		return grantDisbursmentAdvanceLiquidationRef;
//	}

	public List<GrantDisbursmentAdvanceLiquidation> getGrantDisbursmentAdvanceLiquidations() {
		return grantDisbursmentAdvanceLiquidations;
	}

	public void setGrantDisbursmentAdvanceLiquidations(List<GrantDisbursmentAdvanceLiquidation> grantDisbursmentAdvanceLiquidations) {
		this.grantDisbursmentAdvanceLiquidations = grantDisbursmentAdvanceLiquidations;
	}

	public void addGrantDisbursmentAdvanceLiquidation(GrantDisbursmentAdvanceLiquidation liquidation){
		if (this.grantDisbursmentAdvanceLiquidations == null)
			this.grantDisbursmentAdvanceLiquidations = new ArrayList<>();
		liquidation.setInitialAdvanceRef(this);
		this.grantDisbursmentAdvanceLiquidations.add(liquidation);
	}

	public String getPayeesName() {
		return payeesName;
	}

	public void setPayeesName(String payeesName) {
		this.payeesName = payeesName;
	}

	public String getPayeesAccount() {
		return payeesAccount;
	}

	public void setPayeesAccount(String payeesAccount) {
		this.payeesAccount = payeesAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getPayeesAddress() {
		return payeesAddress;
	}

	public void setPayeesAddress(String payeesAddress) {
		this.payeesAddress = payeesAddress;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getSpecialPaymentInstruction() {
		return specialPaymentInstruction;
	}

	public void setSpecialPaymentInstruction(String specialPaymentInstruction) {
		this.specialPaymentInstruction = specialPaymentInstruction;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public String getReassignComments() {
		return reassignComments;
	}

	public void setReassignComments(String reassignComments) {
		this.reassignComments = reassignComments;
	}

	public Date getReassignedOn() {
		return reassignedOn;
	}

	public void setReassignedOn(Date reassignedOn) {
		this.reassignedOn = reassignedOn;
	}



	public List<GrantDisbursmentWithdrawalFiles> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<GrantDisbursmentWithdrawalFiles> attachments) {
		this.attachments = attachments;
	}

	public void addAttachement(GrantDisbursmentWithdrawalFiles a) {
		if(this.attachments == null) {
			this.attachments = new ArrayList<>();
		}

		a.setIaRef(this);

		this.attachments.add(a);
	}
}
