package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Table(name = "quarterly_progress_report_task_reviews")
public class QuarterlyProgressReportTaskReview extends Auditable<String> {
    private long id;
    private String status;
    private String decision;
    private String comments;
    private QuarterlyProgressReportTask qprTaskRef;
    private QuarterlyProgressReport qprRef;
    private Date reviewCompletedOn;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @OneToOne
    @JoinColumn(name = "qpr_task_ref")
    public QuarterlyProgressReportTask getQprTaskRef() {
        return qprTaskRef;
    }

    public void setQprTaskRef(QuarterlyProgressReportTask qprTaskRef) {
        this.qprTaskRef = qprTaskRef;
    }

    @ManyToOne
    @JoinColumn(name = "qpr_ref")
    public QuarterlyProgressReport getQprRef() {
        return qprRef;
    }

    public void setQprRef(QuarterlyProgressReport qprRef) {
        this.qprRef = qprRef;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Date getReviewCompletedOn() {
        return reviewCompletedOn;
    }

    public void setReviewCompletedOn(Date reviewCompletedOn) {
        this.reviewCompletedOn = reviewCompletedOn;
    }
}
