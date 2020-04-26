package com.ndrmf.response;

import java.util.List;

public class FormResponse extends ServiceResponse {

    private List<FormInfo> formInfoList;

    public List<FormInfo> getFormInfoList() {
        return formInfoList;
    }

    public void setFormInfoList(List<FormInfo> formInfoList) {
        this.formInfoList = formInfoList;
    }
}
