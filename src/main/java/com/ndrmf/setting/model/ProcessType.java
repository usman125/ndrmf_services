package com.ndrmf.setting.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "process_types")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ProcessType extends Auditable<String>{
	private String name;
	private User owner;
	private ProcessType parent;

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name="owner_user_id")
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne
	@JoinColumn(name = "parent_name")
	public ProcessType getParent() {
		return parent;
	}

	public void setParent(ProcessType parent) {
		this.parent = parent;
	}
}
