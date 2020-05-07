package com.ndrmf.notification.dto;

import java.util.Date;
import java.util.UUID;

public class NotificationItem{
	private UUID id;
	private Date createdDate;
	private String c2a;
	private String text;
	
	public NotificationItem(UUID id, Date createdDate, String c2a, String text) {
		this.id = id;
		this.createdDate = createdDate;
		this.c2a = c2a;
		this.text = text;
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getC2a() {
		return c2a;
	}
	public void setC2a(String c2a) {
		this.c2a = c2a;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
