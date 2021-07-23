package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "grant_disbursment_advance_liquidation_soes")
public class GrantDisbursmentAdvanceLiquidationSoes extends Auditable<String> {
    private UUID id;
    private InitialAdvance initialAdvanceRef;
    private QuarterAdvance quarterAdvanceRef;
    private GrantDisbursmentAdvanceLiquidation liquidationRef;
    private String data;
    private String soeType;
    private String activityId;
    private String vendorName;
    private Integer invoiceAmount;
    private Date dateOfPayment;
    private Integer paidAmount;
    private String chequeNumber;
    private String remarks;
    private boolean enabled;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "liquidation_ref")
    public GrantDisbursmentAdvanceLiquidation getLiquidationRef() {
        return liquidationRef;
    }

    public void setLiquidationRef(GrantDisbursmentAdvanceLiquidation liquidationRef) {
        this.liquidationRef = liquidationRef;
    }

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSoeType() {
        return soeType;
    }

    public void setSoeType(String soeType) {
        this.soeType = soeType;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public Integer getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Integer invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Date dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
