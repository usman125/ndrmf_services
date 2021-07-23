package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "qpr_to_donor_tasks")
public class QprToDonorTask extends Auditable<String> {



	private String comments;


	private String status;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	private UUID id;

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	private Date startDate;

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	private Date endDate;

	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}


	@ManyToOne
	@JoinColumn(name = "assignee_user_id", nullable = false)
	private User assignee;

	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@ManyToOne
	@JoinColumn(name = "qprtodonor_section_id")
	private QprToDonorSection section;

	public QprToDonorSection getSection() {
		return section;
	}
	public void setSection(QprToDonorSection section) {
		this.section = section;
	}

	@ManyToOne
	@JoinColumn(name = "qprtodonor_id")
	private QprToDonor qprtodonor;

	public void setQprtodonor(QprToDonor qprtodonor) {
		this.qprtodonor = qprtodonor;
	}

	public QprToDonor getQprtodonor() {
		return qprtodonor;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
