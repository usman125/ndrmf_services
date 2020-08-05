package com.ndrmf.engine.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class SectionItem {
	private UUID id;
	private String name;
	private Integer totalScore;
	private Integer passingScore;
	private String templateType;
	private String template;
	private String data;
	private UserLookupItem sme;
	private boolean assigned;
	private List<ReviewItem> reviewHistory;
	private ReviewItem review;
	private String reviewStatus;
	private Date reviewDeadline;
	private Date reviewCompletedDate;
	private String reassignmentStatus;
	private int revisionNo;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getPassingScore() {
		return passingScore;
	}
	public void setPassingScore(Integer passingScore) {
		this.passingScore = passingScore;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public UserLookupItem getSme() {
		return sme;
	}
	public void setSme(UserLookupItem sme) {
		this.sme = sme;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	public ReviewItem getReview() {
		return review;
	}
	
	public void setReview(Date createdDate, String controlWiseComments, String rating, String status, String comments) {
		this.review = new ReviewItem();
		
		this.review.setCreatedDate(createdDate);
		this.review.setControlWiseComments(controlWiseComments);
		this.review.setRating(rating);
		this.review.setStatus(status);
		this.review.setComments(comments);
	}
	
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public List<ReviewItem> getReviewHistory() {
		return reviewHistory;
	}
	public void setReviewHistory(List<ReviewItem> reviewHistory) {
		this.reviewHistory = reviewHistory;
	}
	
	public void addReviewHistory(Date createdDate, String controlWiseComments, String rating, String status, String comments) {
		if(this.reviewHistory == null) {
			this.reviewHistory = new ArrayList<>();
		}
		
		ReviewItem r = new ReviewItem();
		r.setCreatedDate(createdDate);
		r.setControlWiseComments(controlWiseComments);
		r.setRating(rating);
		r.setStatus(status);
		r.setComments(comments);
		
		this.reviewHistory.add(r);
	}
	public String getReassignmentStatus() {
		return reassignmentStatus;
	}
	public void setReassignmentStatus(String reassignmentStatus) {
		this.reassignmentStatus = reassignmentStatus;
	}
	public Date getReviewCompletedDate() {
		return reviewCompletedDate;
	}
	public void setReviewCompletedDate(Date reviewCompletedDate) {
		this.reviewCompletedDate = reviewCompletedDate;
	}
	
	public Date getReviewDeadline() {
		return reviewDeadline;
	}
	public void setReviewDeadline(Date reviewDeadline) {
		this.reviewDeadline = reviewDeadline;
	}
	public int getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(int revisionNo) {
		this.revisionNo = revisionNo;
	}
}
