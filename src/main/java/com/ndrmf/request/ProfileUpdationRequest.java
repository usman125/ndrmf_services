package com.ndrmf.request;

public class ProfileUpdationRequest extends ServiceRequest {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
