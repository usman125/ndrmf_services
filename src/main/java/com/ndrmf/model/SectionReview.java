package com.ndrmf.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

import java.util.Set;

@Entity(name = "section_reviews")
public class SectionReview extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "sec_rev_generator", sequenceName = "sec_rev_seq")
    @GeneratedValue(generator = "sec_rev_generator")
    @Column(name = "sec_review_id")
    private Long id;

    @Size(max = 20)
    private String status;

    @Column(name = "comments")
    private String comments;

    @Column
    private int rating;

    @ManyToOne()
    @JoinColumn(
            name = "section_reviewer",
            referencedColumnName = "username",
            columnDefinition = "varchar(40)",
            foreignKey = @ForeignKey(name = "fkey_sec_rev_username")
    )
    private User sectionReviewer;

    @ManyToOne
    @JoinColumn(
            name = "accreditation_id",
            referencedColumnName = "accreditation_id",
            foreignKey = @ForeignKey(name = "fkey_sec_rev_accred_id")
    )
    private Accreditation accreditation;


    @OneToMany(mappedBy = "sectionReview" , cascade = CascadeType.ALL)
    private Set<ComponentReview> componentReviewSet;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Accreditation getAccreditation() {
        return accreditation;
    }

    public void setAccreditation(Accreditation accreditation) {
        this.accreditation = accreditation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getSectionReviewer() {
        return sectionReviewer;
    }

    public void setSectionReviewer(User sectionReviewer) {
        this.sectionReviewer = sectionReviewer;
    }

    public Set<ComponentReview> getComponentReviewSet() {
        return componentReviewSet;
    }

    public void setComponentReviewSet(Set<ComponentReview> componentReviewSet) {
        this.componentReviewSet = componentReviewSet;
    }

}
