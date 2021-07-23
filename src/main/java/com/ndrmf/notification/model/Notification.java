package com.ndrmf.notification.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ndrmf.user.model.User;

@Entity
@Table(name = "notifications")
public class Notification {
	private UUID id;
	private Date createdDate;
	private String c2a;
	private User to;
	private String text;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
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
	
	@ManyToOne
	@JoinColumn(name = "to_user_id", nullable = false)
	public User getTo() {
		return to;
	}
	public void setTo(User to) {
		this.to = to;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@PrePersist
	public void prePersist() {
		this.createdDate = new Date();
	}
}
