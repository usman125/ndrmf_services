package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddQprTasksRequest {

    private Date startDate;
    private Date endDate;
    private String comments;
    private List<UUID> usersId;

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<UUID> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<UUID> usersId) {
        this.usersId = usersId;
    }

}
