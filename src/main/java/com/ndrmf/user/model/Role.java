package com.ndrmf.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.ndrmf.config.audit.Auditable;

@Entity
@Table(name = "roles")
public class Role extends Auditable<String> implements GrantedAuthority {
	private static final long serialVersionUID = 6892071001682672150L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@ManyToOne
	@JoinColumn(name="org_id", nullable = false)
	private Organisation org;

	@Transient
	@Override
	public String getAuthority() {
		return name;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Organisation getOrg() {
		return org;
	}

	public void setOrg(Organisation org) {
		this.org = org;
	}
}
