package com.ndrmf.engine.dto;
import java.util.Date;
import java.util.UUID;

public class GrantDisbursmentListItem {
    public UUID id;
    public String created_by;
    public Date create_date;
    public String proposalName;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public String getProposalName() {
        return proposalName;
    }
}
