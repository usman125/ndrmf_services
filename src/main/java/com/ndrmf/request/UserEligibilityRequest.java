package com.ndrmf.request;

public class UserEligibilityRequest extends ProfileUpdationRequest {

    private boolean eligible;

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }
}
