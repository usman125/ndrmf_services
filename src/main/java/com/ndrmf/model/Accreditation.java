package com.ndrmf.model;


import javax.persistence.*;
import javax.validation.constraints.Size;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.Section;
import com.ndrmf.user.model.User;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "user_accred_reqs")
public class Accreditation extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "accred_generator", sequenceName = "accred_sequence")
    @GeneratedValue(generator = "accred_generator")
    @Column(name = "accreditation_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "username" , columnDefinition = "varchar(40)",
    nullable = false,
    updatable = false,
    referencedColumnName = "username",
    foreignKey = @ForeignKey(name = "fkey_accred_user_id"))
    private User accUser;

    @ManyToOne
    @JoinColumn(name = "section_key")
    private Section accreditationSection;

    @Column(name = "form_submit_data")
    @Lob
    private String formSubmitData;

    @Column(name = "status")
    @Size(max = 20)
    private String status;

    @Column(name = "form_identity")
    @Size(max = 200)
    private String formIdentity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "request_key")
    @Size(max = 50)
    private String requestKey;

    @Column(name = "user_update_flag")
    private boolean userUpdateFlag;

    @Column(name = "rating")
    private int rating;

    @Column(name = "current_review")
    private String currentReview;

    @Column(name = "prev_review")
    private String prevReview;

    @OneToMany(mappedBy = "accreditation")
    Set<SectionReview> sectionReviewSet;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getAccUser() {
        return accUser;
    }

    public void setAccUser(User accUser) {
        this.accUser = accUser;
    }

    public Section getAccreditationSection() {
        return accreditationSection;
    }

    public void setAccreditationSection(Section accreditationSection) {
        this.accreditationSection = accreditationSection;
    }

    public String getFormSubmitData() {
        return formSubmitData;
    }

    public void setFormSubmitData(String formSubmitData) {
        this.formSubmitData = formSubmitData;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;

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

    public Set<SectionReview> getSectionReviewSet() {
        return sectionReviewSet;
    }

    public void setSectionReviewSet(Set<SectionReview> sectionReviewSet) {
        this.sectionReviewSet = sectionReviewSet;
    }
}
