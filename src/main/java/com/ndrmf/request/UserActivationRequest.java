package com.ndrmf.request;

public class UserActivationRequest extends ProfileUpdationRequest {

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
