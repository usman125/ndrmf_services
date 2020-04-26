package com.ndrmf.response;

import java.io.Serializable;
import java.util.List;

public class UserResponse extends ServiceResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<UserInfo> userInfoList;

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
