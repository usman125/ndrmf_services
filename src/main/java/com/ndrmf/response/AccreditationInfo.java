package com.ndrmf.response;

import java.io.Serializable;
import java.util.Date;

public class AccreditationInfo implements Serializable {

    private String userName;
    private String sectionKey;
    private String  formSubmitData;
    private String formData;
    private String status;
    private String formIdentity;
    private Date startDate;
    private Date endDate;
    private String requestKey;
    private boolean userUpdateFlag;
    private int ratings;
    private String currentReview;
    private String prevReview;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getFormSubmitData() {
        return formSubmitData;
    }

    public void setFormSubmitData(String formSubmitData) {
        this.formSubmitData = formSubmitData;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormIdentity() {
        return formIdentity;
    }

    public void setFormIdentity(String formIdentity) {
        this.formIdentity = formIdentity;
    }

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

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public boolean isUserUpdateFlag() {
        return userUpdateFlag;
    }

    public void setUserUpdateFlag(boolean userUpdateFlag) {
        this.userUpdateFlag = userUpdateFlag;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getCurrentReview() {
        return currentReview;
    }

    public void setCurrentReview(String currentReview) {
        this.currentReview = currentReview;
    }

    public String getPrevReview() {
        return prevReview;
    }

    public void setPrevReview(String prevReview) {
        this.prevReview = prevReview;
    }
}
