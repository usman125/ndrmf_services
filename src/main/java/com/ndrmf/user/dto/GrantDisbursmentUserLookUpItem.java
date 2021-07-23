package com.ndrmf.user.dto;

import java.util.List;
import java.util.UUID;

public class GrantDisbursmentUserLookUpItem {
    private final UUID id;
    private final String name;
    private String jvUsername;
    private String type;

    public GrantDisbursmentUserLookUpItem(
            UUID id,
            String name,
            String jvUsername,
            String type) {
        this.id = id;
        this.name = name;
        this.jvUsername = jvUsername;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    public String getJvUsername() {
        return jvUsername;
    }
}
