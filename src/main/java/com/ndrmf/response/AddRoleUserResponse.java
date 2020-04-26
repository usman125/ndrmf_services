package com.ndrmf.response;

import java.util.Set;

public class AddRoleUserResponse extends ServiceResponse {

    private String username;
    private Set<String> roleSet;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<String> roleSet) {
        this.roleSet = roleSet;
    }
}
