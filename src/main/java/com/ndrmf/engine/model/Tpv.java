package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "tpv")
public class Tpv extends Auditable<String> {
	private UUID id;
	private ProjectProposal projectProposalRef;
	private User initiatedBy;
	private String status;
	private String generalComments;
	private Date created_at;
	private List<TpvTasks> tasks;
	
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
	
	@OneToMany(mappedBy="tpvRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<TpvTasks> getTasks() {
		return tasks;
	}
	public void setTasks(List<TpvTasks> tasks) {
		this.tasks = tasks;
	}

	public void addTask(TpvTasks task) {
		if(this.tasks == null) {
			this.tasks = new ArrayList<TpvTasks>();
		}
		task.setTpvRef(this);
		this.tasks.add(task);
	}
//
//	@Type(type = "jsonb")
//	@Column(columnDefinition = "jsonb")
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
