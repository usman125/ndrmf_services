package com.ndrmf.engine.dto;

public class GrantDisbursmentAdvanceRequest {
    public String data;
    public Integer amount;

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
}
