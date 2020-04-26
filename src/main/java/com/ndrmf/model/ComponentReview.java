package com.ndrmf.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.ndrmf.config.audit.Auditable;

@Entity(name = "component_reviews")
public class ComponentReview extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "comp_rev_generator", sequenceName = "comp_rev_sequence")
    @GeneratedValue(generator = "comp_rev_generator")
    @Column(name = "component_review_id")
    private Long id;

    @Size(max = 200)
    private String title;

    @Size(max = 200)
    private String value;

    @Size(max = 10)
    @Column(name = "submit_value")
    private String submitValue;

    @Size(max = 10)
    private String key;

    @Column
    private int rating;

    @Size(max = 20)
    private String status;

    @Column
    private String comments;

    @ManyToOne
    @JoinColumn(
            name = "sec_review_id",
            referencedColumnName = "sec_review_id",
            foreignKey = @ForeignKey(name = "fkey_comp_sec_review_id")
    )
    private SectionReview sectionReview;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubmitValue() {
        return submitValue;
    }

    public void setSubmitValue(String submitValue) {
        this.submitValue = submitValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public SectionReview getSectionReview() {
        return sectionReview;
    }

    public void setSectionReview(SectionReview sectionReview) {
        this.sectionReview = sectionReview;
    }
}
