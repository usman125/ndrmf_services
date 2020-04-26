package com.ndrmf.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ndrmf.config.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "sections")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sectionKey")
public class Section extends Auditable<String> {
    @Id
    @Size(max = 45)
    @Column(name = "section_key")
    private String sectionKey;

    @Size(max = 200)
    @Column(name = "section_name", updatable = false)
    private String sectionName;

    @Column(name = "form_Generated", columnDefinition = "boolean default false")
    private boolean isFormGenerated;

    @Column(name = "user_name")
    @Size(max = 40)
    private String username;

    @Column(name = "formIdentity")
    @Size(max = 50)
    private String formIdentity;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean active = true;

    @OneToOne(mappedBy = "section")
    private Form form;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "accreditationSection")
    private Set<Accreditation> accreditations;


    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public boolean isFormGenerated() {
        return isFormGenerated;
    }

    public void setFormGenerated(boolean formGenerated) {
        isFormGenerated = formGenerated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getFormIdentity() {
        return formIdentity;
    }

    public void setFormIdentity(String formIdentity) {
        this.formIdentity = formIdentity;
    }

    public Set<Accreditation> getAccreditations() {
        return accreditations;
    }

    public void setAccreditations(Set<Accreditation> accreditations) {
        this.accreditations = accreditations;
    }
}
