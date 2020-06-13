package com.ndrmf.engine.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class GrantImplmentationItem {
	private UserLookupItem processOwner;
	private String data;
	private String status;
	private String subStatus;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	private List<GiaReviewItem> reviews;
	
	public UserLookupItem getProcessOwner() {
		return processOwner;
	}
	public void setProcessOwner(UserLookupItem processOwner) {
		this.processOwner = processOwner;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public List<GiaReviewItem> getReviews() {
		return reviews;
	}
	public void setReviews(List<GiaReviewItem> reviews) {
		this.reviews = reviews;
	}
	public void addReview(GiaReviewItem review) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<>();
		}
		
		this.reviews.add(review);
	}

	public static class GiaReviewItem {
		private UUID id;
		private UserLookupItem assignee;
		private boolean isAssigned;
		private String comments;
		public UUID getId() {
			return id;
		}
		public void setId(UUID id) {
			this.id = id;
		}
		public UserLookupItem getAssignee() {
			return assignee;
		}
		public void setAssignee(UserLookupItem assignee) {
			this.assignee = assignee;
		}
		public boolean isAssigned() {
			return isAssigned;
		}
		public void setAssigned(boolean isAssigned) {
			this.isAssigned = isAssigned;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
	}
}
