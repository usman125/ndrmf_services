package com.ndrmf.engine.dto;
import java.util.List;

public class GrantDisbursmentAdvanceLiquidationSubmitRequest {
    private String payeesName;
    private String payeesAddress;
    private String bankName;
    private String bankAddress;
    private String payeesAccount;
    private String swiftCode;
    private String specialPaymentInstruction;
    private List<GrantDisbursmentAdvanceLiquidationSoeSubmitRequest> ndrmfSoes;
    private List<GrantDisbursmentAdvanceLiquidationSoeSubmitRequest> fipSoes;


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

    public List<GrantDisbursmentAdvanceLiquidationSoeSubmitRequest> getNdrmfSoes() {
        return ndrmfSoes;
    }

    public void setNdrmfSoes(List<GrantDisbursmentAdvanceLiquidationSoeSubmitRequest> ndrmfSoes) {
        this.ndrmfSoes = ndrmfSoes;
    }

    public List<GrantDisbursmentAdvanceLiquidationSoeSubmitRequest> getFipSoes() {
        return fipSoes;
    }

    public void setFipSoes(List<GrantDisbursmentAdvanceLiquidationSoeSubmitRequest> fipSoes) {
        this.fipSoes = fipSoes;
    }
}
