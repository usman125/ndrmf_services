package com.ndrmf.response;

public class FormUpdateResponse extends ServiceResponse {

    private FormInfo formInfo;

    public FormInfo getFormInfo() {
        return formInfo;
    }

    public void setFormInfo(FormInfo formInfo) {
        this.formInfo = formInfo;
    }
}
