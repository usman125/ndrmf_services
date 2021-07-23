package com.ndrmf.user.dto;

import com.ndrmf.user.model.Role;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QprUserLookUpItem {
    private final UUID id;
    private final String name;
    private List<String> roles;
    private String department;

    public QprUserLookUpItem(UUID id, String name, List<String> roles, String department) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.department = department;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
