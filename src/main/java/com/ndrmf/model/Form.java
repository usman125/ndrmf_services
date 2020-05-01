package com.ndrmf.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.Section;

@Entity(name = "forms")
public class Form extends Auditable<String> {

    @Id
    @Column(name = "form_id")
    @SequenceGenerator(name = "form_generator", sequenceName = "form_sequence")
    @GeneratedValue(generator = "form_generator")
    private long id;

    @Size(max = 100)
    private String name;

    @Size(max = 20)
    @Column(name = "display")
    private String displayType;

    @Size(max = 20)
    @Column(name = "form_identity")
    private String formIdentity;

    @Column(name = "pass_score")
    private int passingScore;

    @Column(name = "total_score")
    private int totalScore;

    @Column(name = "page")
    private int page;

    @Column(name = "total_pages")
    private int numOfPages;

    @Size(max = 10)
    private String status;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean active;

    @Lob
    private String component;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "section_key")
    private Section section;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getFormIdentity() {
        return formIdentity;
    }

    public void setFormIdentity(String formIdentity) {
        this.formIdentity = formIdentity;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
