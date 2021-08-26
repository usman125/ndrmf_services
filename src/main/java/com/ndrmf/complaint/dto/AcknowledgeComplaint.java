package com.ndrmf.complaint.dto;

import java.util.List;
import java.util.UUID;

public class AcknowledgeComplaint {

	private String status;
	private Long seqNo;
	private String priority;
	private String internalStatus;
	private List<Assignee> assignee;

	public AcknowledgeComplaint() {
	}

	public AcknowledgeComplaint(
			String status,
			Long seqNo,
			String priority, String internalStatus,
			List<Assignee> assignee) {
		super();
		this.status = status;
		this.seqNo = seqNo;
		this.priority = priority;
		this.internalStatus = internalStatus;
		this.assignee = assignee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getInternalStatus() {
		return internalStatus;
	}

	public void setInternalStatus(String internalStatus) {
		this.internalStatus = internalStatus;
	}

	public List<Assignee> getAssignee() {
		return assignee;
	}

	public void setAssignee(List<Assignee> assignedUser) {
		this.assignee = assignedUser;
	}

	public static class Assignee {
		private UUID userId;

		public Assignee() {}

		public UUID getUserId() {
			return userId;
		}

		public void setUserId(UUID userId) {
			this.userId = userId;
		}

	}

}
