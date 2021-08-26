package com.ndrmf.engine.model;
import com.ndrmf.config.audit.Auditable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.List;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.mapstruct.Mapping;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "grant_disbursment_advance_liquidation")
public class GrantDisbursmentAdvanceLiquidation extends Auditable<String> {
    private UUID id;
    private InitialAdvance initialAdvanceRef;
    private QuarterAdvance quarterAdvanceRef;
    private String data;
    private String status;
    private String subStatus;
    private String comments;
    private Integer amount;
    private String payeesName;
    private String payeesAddress;
    private String bankName;
    private String bankAddress;
    private String payeesAccount;
    private String swiftCode;
    private String specialPaymentInstruction;
    private List<GrantDisbursmentAdvanceLiquidationSoes> liquidationSoes;
    private String reassignmentComments;
    private Integer orderNumber;
    private Date reassignedOn;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "initial_advance_id")
    public InitialAdvance getInitialAdvanceRef() {
        return initialAdvanceRef;
    }

    public void setInitialAdvanceRef(InitialAdvance initialAdvanceRef) {
        this.initialAdvanceRef = initialAdvanceRef;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "quarter_advance_id")
    public QuarterAdvance getQuarterAdvanceRef() {
        return quarterAdvanceRef;
    }

    public void setQuarterAdvanceRef(QuarterAdvance quarterAdvanceRef) {
        this.quarterAdvanceRef = quarterAdvanceRef;
    }


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    @OneToMany(mappedBy="liquidationRef", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    public List<GrantDisbursmentAdvanceLiquidationSoes> getLiquidationSoes() {
        return liquidationSoes;
    }

    public void setLiquidationSoes(List<GrantDisbursmentAdvanceLiquidationSoes> liquidationSoes) {
        this.liquidationSoes = liquidationSoes;
    }

    public void addLiquidationSoe(GrantDisbursmentAdvanceLiquidationSoes soe) {
        if(this.liquidationSoes == null) {
            this.liquidationSoes = new ArrayList<GrantDisbursmentAdvanceLiquidationSoes>();
        }
        soe.setLiquidationRef(this);
        this.liquidationSoes.add(soe);
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
