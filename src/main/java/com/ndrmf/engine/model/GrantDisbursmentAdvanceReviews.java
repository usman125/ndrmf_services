package com.ndrmf.engine.model;
import com.ndrmf.user.model.User;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ndrmf.config.audit.Auditable;

@Entity
@Table(name = "grant_disbursment_advance_reviews")
public class GrantDisbursmentAdvanceReviews extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private String comments;
    private String status;
    private String subStatus;


    @ManyToOne
    @JoinColumn(name = "initial_advance_id", nullable = false)
    private InitialAdvance initialAdvanceRef;

    @ManyToOne
    @JoinColumn(name = "quarter_advance_id", nullable = false)
    private QuarterAdvance quarterAdvanceRef;

    @ManyToOne
    @JoinColumn(name = "assignee_user_id", nullable = false)
    private User assignee;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public void setInitialAdvanceRef(InitialAdvance initialAdvanceRef) {
        this.initialAdvanceRef = initialAdvanceRef;
    }

    public InitialAdvance getInitialAdvanceRef() {
        return initialAdvanceRef;
    }

    public void setQuarterAdvanceRef(QuarterAdvance quarterAdvanceRef) {
        this.quarterAdvanceRef = quarterAdvanceRef;
    }

    public QuarterAdvance getQuarterAdvanceRef() {
        return quarterAdvanceRef;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getAssignee() {
        return assignee;
    }
}
