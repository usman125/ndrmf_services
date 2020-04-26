package com.ndrmf.response;

import java.util.List;

public class UserGroupResponse extends ServiceResponse {

    List<String> groupNames;

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }
}
