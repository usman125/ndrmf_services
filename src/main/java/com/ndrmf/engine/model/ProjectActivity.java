package com.ndrmf.engine.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "project_activities")
public class ProjectActivity {
	private long id;
	private String name;
	private boolean isProcurement;
	private ProjectActivity parent;
	private ProjectActivity child;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isProcurement() {
		return isProcurement;
	}
	public void setProcurement(boolean isProcurement) {
		this.isProcurement = isProcurement;
	}
	
	@ManyToOne
	@JoinColumn(name="parent_id")
	public ProjectActivity getParent() {
		return parent;
	}
	public void setParent(ProjectActivity parent) {
		this.parent = parent;
	}
}
