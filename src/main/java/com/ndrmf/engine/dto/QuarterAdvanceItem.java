package com.ndrmf.engine.dto;

import com.ndrmf.user.dto.UserLookupItem;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.Date;

public class QuarterAdvanceItem {
    public String status;
    public Integer amount;
    public String data;
    public UUID id;
    public String subStatus;
    private List<QuarterAdvanceReviewsListItem> quarterAdvanceReviewsList;
    private List<AdvanceLiquidationItem> advanceLiquidations;

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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public List<QuarterAdvanceReviewsListItem> getQuarterAdvanceReviewsList() {
        return quarterAdvanceReviewsList;
    }

    public void setQuarterAdvanceReviewsList(List<QuarterAdvanceReviewsListItem> quarterAdvanceReviewsList) {
        this.quarterAdvanceReviewsList = quarterAdvanceReviewsList;
    }

    public List<AdvanceLiquidationItem> getAdvanceLiquidations() {
        return advanceLiquidations;
    }

    public void setAdvanceLiquidations(List<AdvanceLiquidationItem> advanceLiquidations) {
        this.advanceLiquidations = advanceLiquidations;
    }

    public void addAdvanceLiquidation(AdvanceLiquidationItem liquidation){
        if (this.getAdvanceLiquidations() == null){
            this.advanceLiquidations = new ArrayList<>();
        }
        this.advanceLiquidations.add(liquidation);
    }

    public void addQuarterAdvanceReviewsList(QuarterAdvanceReviewsListItem review){
            if (this.getQuarterAdvanceReviewsList() == null){
                this.quarterAdvanceReviewsList = new ArrayList<>();
            }
            this.quarterAdvanceReviewsList.add(review);
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

    public static class QuarterAdvanceReviewsListItem {
        private UUID id;
        private UserLookupItem assignee;
        private boolean isAssigned;
        private String comments;
        private String status;
        private String subStatus;
        private int amount;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public UUID getId() {
            return id;
        }
        public void setId(UUID id) {
            this.id = id;
        }
        public UserLookupItem getAssignee() {
            return assignee;
        }
        public void setAssignee(UserLookupItem assignee) {
            this.assignee = assignee;
        }
        public boolean isAssigned() {
            return isAssigned;
        }
        public void setAssigned(boolean isAssigned) {
            this.isAssigned = isAssigned;
        }
        public String getComments() {
            return comments;
        }
        public void setComments(String comments) {
            this.comments = comments;
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
    }

    public static class AdvanceLiquidationItem {
        private UUID id;
        private String data;
        private Integer amount;
        private String comments;
        private String status;
        private String subStatus;

        private String payeesName;
        private String payeesAddress;
        private String bankName;
        private String bankAddress;
        private String payeesAccount;
        private String swiftCode;
        private String specialPaymentInstruction;

        private String reassignmentComments;
        private Date reassignedOn;

        private List<GrantDisbursmentAdvanceLiquidationSoesItem> ndrmfSoes;
        private List<GrantDisbursmentAdvanceLiquidationSoesItem> fipSoes;

        private Integer orderNumber;

        public String getReassignmentComments() {
            return reassignmentComments;
        }

        public void setReassignmentComments(String reassignmentComments) {
            this.reassignmentComments = reassignmentComments;
        }

        public UUID getId() {
            return id;
        }
        public void setId(UUID id) {
            this.id = id;
        }

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

        public String getComments() {
            return comments;
        }
        public void setComments(String comments) {
            this.comments = comments;
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

        public List<GrantDisbursmentAdvanceLiquidationSoesItem> getFipSoes() {
            return fipSoes;
        }

        public void setFipSoes(List<GrantDisbursmentAdvanceLiquidationSoesItem> fipSoes) {
            this.fipSoes = fipSoes;
        }

        public void addFipSoe(GrantDisbursmentAdvanceLiquidationSoesItem fipSoe){
            if (this.fipSoes == null){
                this.fipSoes  = new ArrayList<>();
            }
            this.fipSoes.add(fipSoe);
        }

        public List<GrantDisbursmentAdvanceLiquidationSoesItem> getNdrmfSoes() {
            return ndrmfSoes;
        }

        public void setNdrmfSoes(List<GrantDisbursmentAdvanceLiquidationSoesItem> ndrmfSoes) {
            this.ndrmfSoes = ndrmfSoes;
        }

        public void addNdrmfSoe(GrantDisbursmentAdvanceLiquidationSoesItem ndrmfSoe){
            if (this.ndrmfSoes == null){
                this.ndrmfSoes = new ArrayList<>();
            }
            this.ndrmfSoes.add(ndrmfSoe);
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

}
