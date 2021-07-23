package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "subproject_document_tasks")
public class SubProjectDocumentTasks extends Auditable<String> {
	private UUID id;
	private String status;
	private String subStatus;
	private User assignee;
	private User assigneedBy;
	private Date startDate;
	private Date endDate;
	private Date completedOn;
	private SubProjectDocumentDmPamTasks subProjectDocumentDmPamTasksRef;
	private String remarks;
	private String comments;
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

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}



	@ManyToOne
	@JoinColumn(name = "subprojectdocumentdmpamtask_id", nullable = false)
	public SubProjectDocumentDmPamTasks getSubProjectDocumentDmPamTasksRef() {
		return subProjectDocumentDmPamTasksRef;
	}
	public void setSubProjectDocumentDmPamTasksRef(SubProjectDocumentDmPamTasks subProjectDocumentDmPamTasksRef) {
		this.subProjectDocumentDmPamTasksRef = subProjectDocumentDmPamTasksRef;
	}
	
	@ManyToOne
	@JoinColumn(name = "assignee_user_id", nullable = false)
	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@ManyToOne
	@JoinColumn(name = "assigneed_by_user_id", nullable = false)
	public User getAssigneedBy() {
		return assigneedBy;
	}
	public void setAssigneedBy(User assigneedBy) {
		this.assigneedBy = assigneedBy;
	}

	@Column(columnDefinition = "DATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(columnDefinition = "DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(columnDefinition = "DATE")
	public Date getCompletedOn() {
		return completedOn;
	}
	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

//	@Type(type = "jsonb")
//	@Column(columnDefinition = "jsonb")
//	public String getGeneralComments() {
//		return generalComments;
//	}
//	public void setGeneralComments(String generalComments) {
//		this.generalComments = generalComments;
//	}
}
