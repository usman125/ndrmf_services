package com.ndrmf.request;

public class UserQualificationRequest extends ProfileUpdationRequest {

    private boolean qualified;

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }
}
