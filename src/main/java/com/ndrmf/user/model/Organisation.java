package com.ndrmf.user.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ndrmf.config.audit.Auditable;

@Entity
@Table(name = "organisations")
public class Organisation extends Auditable<String> implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
    @Size(max = 50)
    @Column(unique = true)
	private String name;

	@OneToMany(mappedBy="org", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Role> roles;

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

	public List<Role> getRoles(){
		return this.roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
