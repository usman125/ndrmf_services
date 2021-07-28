package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.util.enums.ProcessStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;


@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "quarter_advance")
public class QuarterAdvance extends Auditable<String> {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID Id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String data;

    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "grant_disbursment_id", nullable = false)
    private GrantDisbursment grantDisbursmentRef;

    private String status;
    private String subStatus;

    @OneToMany(mappedBy = "quarterAdvanceRef", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<GrantDisbursmentAdvanceReviews> reviewsList;

    @OneToMany(mappedBy = "quarterAdvanceRef", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<GrantDisbursmentAdvanceLiquidation> grantDisbursmentAdvanceLiquidations;

    private String payeesName;
    private String payeesAddress;
    private String bankName;
    private String bankAddress;
    private String payeesAccount;
    private String swiftCode;
    private String specialPaymentInstruction;

    private Integer quarter;

    private String reassignmentComments;

    private Date reassignedOn;

    @OneToMany(mappedBy = "qaRef", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GrantDisbursmentWithdrawalFiles> attachments;

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

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public GrantDisbursment getGrantDisbursmentRef() {
        return grantDisbursmentRef;
    }

    public void setGrantDisbursmentRef(GrantDisbursment grantDisbursmentRef) {
        this.grantDisbursmentRef = grantDisbursmentRef;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        if (this.reviewsList == null) {
            this.reviewsList = new ArrayList<>();
        }

        review.setQuarterAdvanceRef(this);
        this.reviewsList.add(review);
    }

//    public GrantDisbursmentAdvanceLiquidation getGrantDisbursmentAdvanceLiquidationRef() {
//        return grantDisbursmentAdvanceLiquidationRef;
//    }
//
//    public void setGrantDisbursmentAdvanceLiquidationRef(GrantDisbursmentAdvanceLiquidation grantDisbursmentAdvanceLiquidationRef) {
//        this.grantDisbursmentAdvanceLiquidationRef = grantDisbursmentAdvanceLiquidationRef;
//    }

    public List<GrantDisbursmentAdvanceLiquidation> getGrantDisbursmentAdvanceLiquidations() {
        return grantDisbursmentAdvanceLiquidations;
    }

    public void setGrantDisbursmentAdvanceLiquidations(List<GrantDisbursmentAdvanceLiquidation> grantDisbursmentAdvanceLiquidations) {
        this.grantDisbursmentAdvanceLiquidations = grantDisbursmentAdvanceLiquidations;
    }

    public void addGrantDisbursmentAdvanceLiquidation(GrantDisbursmentAdvanceLiquidation liquidation) {
        if (this.grantDisbursmentAdvanceLiquidations == null)
            this.grantDisbursmentAdvanceLiquidations = new ArrayList<>();
        liquidation.setQuarterAdvanceRef(this);
        this.grantDisbursmentAdvanceLiquidations.add(liquidation);
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public String getReassignmentComments() {
        return reassignmentComments;
    }

    public void setReassignmentComments(String reassignmentComments) {
        this.reassignmentComments = reassignmentComments;
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
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }

        a.setQaRef(this);

        this.attachments.add(a);
    }
}
