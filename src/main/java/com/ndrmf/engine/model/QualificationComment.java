package com.ndrmf.engine.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ndrmf.user.model.User;

@Entity
@Table(name = "qualification_comments")
public class QualificationComment {
	private long id;
	private User by;
	private boolean forFip;
	private Qualification qualification;
	private QualificationSection section;
	private Date createdDate;
	private String text;
	private QualificationComment thread;
	private Set<QualificationComment> replies;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "by_user_id", nullable = false)
	public User getBy() {
		return by;
	}
	public void setBy(User by) {
		this.by = by;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "thread_id")
	public QualificationComment getThread() {
		return thread;
	}
	public void setThread(QualificationComment thread) {
		this.thread = thread;
	}
	
	@OneToMany(mappedBy = "thread")
	public Set<QualificationComment> getReplies() {
		return replies;
	}
	public void setReplies(Set<QualificationComment> replies) {
		this.replies = replies;
	}
	
	public void addReply(QualificationComment comment) {
		if(this.replies == null) {
			this.replies = new HashSet<>();
		}
		
		comment.setThread(this);
		this.replies.add(comment);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isForFip() {
		return forFip;
	}
	public void setForFip(boolean forFip) {
		this.forFip = forFip;
	}
	@ManyToOne
	@JoinColumn(name = "qualification_id", nullable = false)
	public Qualification getQualification() {
		return qualification;
	}
	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}
	@PrePersist
	public void prePersist() {
		this.createdDate = new Date();
	}
	
	@ManyToOne
	@JoinColumn(name = "qual_section_id", nullable = true)
	public QualificationSection getSection() {
		return section;
	}
	public void setSection(QualificationSection section) {
		this.section = section;
	}
}