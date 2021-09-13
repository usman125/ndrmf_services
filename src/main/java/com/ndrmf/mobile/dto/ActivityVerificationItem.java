package com.ndrmf.mobile.dto;

import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.mobile.model.ActivityVerificationFiles;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ActivityVerificationItem {
    private UUID id;
    private String proposalName;
    private UserLookupItem initiatedBy;
    private String status;
    private String generalComments;
    private Date created_at;
    private int quarter;
    private String activity;
    private int rating;
    private List<ActivityVerificationFilesListItem> files;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProposalName() {
        return proposalName;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public UserLookupItem getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(UserLookupItem initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getGeneralComments() {
        return generalComments;
    }

    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public List<ActivityVerificationFilesListItem> getFiles() {
        return files;
    }

    public void setFiles(List<ActivityVerificationFilesListItem> files) {
        this.files = files;
    }

    public void addFile(ActivityVerificationFilesListItem file){
        if (this.files == null)
            this.files = new ArrayList<>();
        this.files.add(file);
    }
}
