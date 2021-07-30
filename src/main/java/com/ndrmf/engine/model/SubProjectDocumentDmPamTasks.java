package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
//import javax.persistence.Type;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

//import java.time.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Entity
@Table(name = "subproject_document_dmpam_tasks")
public class SubProjectDocumentDmPamTasks extends Auditable<String> {
	private UUID id;
	private String status;
	private String subStatus;
	private User assignee;
	private User assigneedBy;
	private Date startDate;
	private Date endDate;
	private Date completedOn;
	private SubProjectDocument subProjectRef;
	private List<SubProjectDocumentTasks> tasks;
	private String comments;
	private String generalComments;
	private String dmComments;
	
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
	@JoinColumn(name = "subprojectdocument_id", nullable = false)
	public SubProjectDocument getSubProjectRef() {
		return subProjectRef;
	}

	public void setSubProjectRef(SubProjectDocument subProjectRef) {
		this.subProjectRef = subProjectRef;
	}

	@OneToMany(mappedBy="subProjectDocumentDmPamTasksRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<SubProjectDocumentTasks> getTasks() {
		return tasks;
	}
	public void setTasks(List<SubProjectDocumentTasks> tasks) {
		this.tasks = tasks;
	}

	public void addTask(SubProjectDocumentTasks task) {
		if (this.tasks == null) {
			this.tasks = new ArrayList<SubProjectDocumentTasks>();
		}

		task.setSubProjectDocumentDmPamTasksRef(this);

		this.tasks.add(task);
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

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getGeneralComments() {
		return generalComments;
	}
	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	public String getDmComments() {
		return dmComments;
	}

	public void setDmComments(String dmComments) {
		this.dmComments = dmComments;
	}
}
