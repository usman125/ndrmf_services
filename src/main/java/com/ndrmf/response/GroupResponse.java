package com.ndrmf.response;

import java.io.Serializable;
import java.util.List;

public class GroupResponse extends ServiceResponse implements Serializable {

    private List<String> groupNames;

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }
}
