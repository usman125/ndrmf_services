package com.ndrmf.engine.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.ndrmf.util.KeyValue;

public class GeneralCommentItem {
	private List<String> sections;
	private String addedBy;
	private String comment;
	private Date createdAt;
	private Set<KeyValue> sectionsWithIds;
	
	public List<String> getSections() {
		return sections;
	}
	public void setSections(List<String> sections) {
		this.sections = sections;
	}
	public String getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(String addedBy) {
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
	public Set<KeyValue> getSectionsWithIds() {
		return sectionsWithIds;
	}
	public void setSectionsWithIds(Set<KeyValue> sectionsWithIds) {
		this.sectionsWithIds = sectionsWithIds;
	}
}
