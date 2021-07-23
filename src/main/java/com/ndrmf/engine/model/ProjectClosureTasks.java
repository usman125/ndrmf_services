package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "project_closure_tasks")
public class ProjectClosureTasks extends Auditable<String> {
	private UUID id;
	private User initiatedBy;
	private User assignee;
	private String status;
	private String generalFields;
	private Date created_at;
	private ProjectClosure projectClosureRef;
	private Integer orderNum;
	private String generalComments;

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


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "assignee_user_id")
	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getGeneralFields() {
		return generalFields;
	}
	public void setGeneralFields(String generalFields) {
		this.generalFields = generalFields;
	}

	@ManyToOne
	@JoinColumn(name = "project_closure_id")
	public ProjectClosure getProjectClosureRef() {
		return projectClosureRef;
	}
	public void setProjectClosureRef(ProjectClosure projectClosureRef) {
		this.projectClosureRef = projectClosureRef;
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

	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer order) {
		this.orderNum = order;
	}

	public String getGeneralComments() {
		return generalComments;
	}

	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}
}
