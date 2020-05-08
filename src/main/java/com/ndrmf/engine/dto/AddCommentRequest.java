package com.ndrmf.engine.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

public class AddCommentRequest {
	private boolean toFip;
	private String text;
	private Long threadId;
	
	public boolean isToFip() {
		return toFip;
	}
	public void setToFip(boolean toFip) {
		this.toFip = toFip;
	}
	
	@NotBlank
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public Long getThreadId() {
		return threadId;
	}
	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}
}
