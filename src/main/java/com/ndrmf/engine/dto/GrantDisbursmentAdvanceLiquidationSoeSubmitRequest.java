package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.UUID;

public class GrantDisbursmentAdvanceLiquidationSoeSubmitRequest {
    private String soeType;
    private String activityId;
    private String vendorName;
    private Integer invoiceAmount;
    private Date dateOfPayment;
    private Integer paidAmount;
    private String chequeNumber;
    private String remarks;
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
