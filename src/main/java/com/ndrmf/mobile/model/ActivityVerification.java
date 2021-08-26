package com.ndrmf.mobile.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.user.model.User;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "activity_verifications")
public class ActivityVerification extends Auditable<String> {
	private UUID id;
	private ProjectProposal projectProposalRef;
	private User initiatedBy;
	private String status;
	private String generalComments;
	private Date created_at;
	private int quarter;
	private String activityId;
	private int rating;
	private List<ActivityVerificationFiles> files;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "initiator_user_id")
	public User getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(User initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	@ManyToOne
	@JoinColumn(name = "project_proposal_id")
	public ProjectProposal getProjectProposalRef() {
		return projectProposalRef;
	}
	public void setProjectProposalRef(ProjectProposal projectProposalRef) {
		this.projectProposalRef = projectProposalRef;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	@OneToMany(mappedBy="avRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ActivityVerificationFiles> getFiles() {
		return files;
	}
	public void setFiles(List<ActivityVerificationFiles> files) {
		this.files = files;
	}

	public void addFile(ActivityVerificationFiles file) {
		if(this.files == null) {
			this.files = new ArrayList<ActivityVerificationFiles>();
		}
		file.setAvRef(this);
		this.files.add(file);
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getGeneralComments() {
		return generalComments;
	}
	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
