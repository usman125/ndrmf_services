package com.ndrmf.request;

public class TypeRequest extends ServiceRequest {

    private String typeName;
    private boolean active;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
