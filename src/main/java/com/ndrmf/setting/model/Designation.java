package com.ndrmf.setting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ndrmf.config.audit.Auditable;

@Entity
@Table(name = "designations")
public class Designation extends Auditable<String> {
	private int id;
	private String name;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@NotBlank
    @Size(max = 100)
    @Column(unique = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
