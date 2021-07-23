package com.ndrmf.engine.model;

import java.util.Date;
import java.util.List;

import com.ndrmf.util.KeyValue;

public class ProjectProposalGeneralCommentModel {
	private List<KeyValue> sections;
	private String stage;
	private KeyValue addedBy;
	private String comment;
	private Date createdAt;
	
	public List<KeyValue> getSections() {
		return sections;
	}
	public void setSections(List<KeyValue> sections) {
		this.sections = sections;
	}
	public KeyValue getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(KeyValue addedBy) {
		this.addedBy = addedBy;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
}
