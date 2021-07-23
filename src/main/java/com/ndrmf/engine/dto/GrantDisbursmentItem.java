package com.ndrmf.engine.dto;
import java.util.List;
import java.util.UUID;

import com.ndrmf.engine.model.InitialAdvance;
import com.ndrmf.engine.model.QuarterAdvance;
import com.ndrmf.user.dto.GrantDisbursmentUserLookUpItem;
import com.ndrmf.user.dto.UserLookupItem;

public class GrantDisbursmentItem {
    public UUID id;
    public String proposalName;
    public String implementationPlan;
    public InitialAdvanceItem initialAdvance;
    public List<QuarterAdvanceItem> quarterAdvanceList;
    public String status;
    public String subStatus;
    public UserLookupItem owner;
    public boolean assigned;
    public String initAdvanceStatus;
    public String quarterAdvanceStatus;
    public GrantDisbursmentUserLookUpItem initiatedBy;


    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public String getProposalName() {
        return proposalName;
    }

    public void setInitialAdvance(InitialAdvanceItem initialAdvance) {
        this.initialAdvance = initialAdvance;
    }

    public InitialAdvanceItem getInitialAdvance() {
        return initialAdvance;
    }

    public void setQuarterAdvanceList(List<QuarterAdvanceItem> quarterAdvanceList) {
        this.quarterAdvanceList = quarterAdvanceList;
    }

    public List<QuarterAdvanceItem> getQuarterAdvanceList() {
        return quarterAdvanceList;
    }

    public void setImplementationPlan(String implementationPlan) {
        this.implementationPlan = implementationPlan;
    }

    public String getImplementationPlan() {
        return implementationPlan;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public UserLookupItem getOwner() {
        return owner;
    }

    public void setOwner(UserLookupItem owner) {
        this.owner = owner;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getInitAdvanceStatus() {
        return initAdvanceStatus;
    }

    public void setInitAdvanceStatus(String initAdvanceStatus) {
        this.initAdvanceStatus = initAdvanceStatus;
    }

    public String getQuarterAdvanceStatus() {
        return quarterAdvanceStatus;
    }

    public void setQuarterAdvanceStatus(String quarterAdvanceStatus) {
        this.quarterAdvanceStatus = quarterAdvanceStatus;
    }

    public GrantDisbursmentUserLookUpItem getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(GrantDisbursmentUserLookUpItem initiatedBy) {
        this.initiatedBy = initiatedBy;
    }
}
