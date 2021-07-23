package com.ndrmf.engine.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.user.model.User;

@Entity
@Table(name = "fip_thematic_areas")
public class FIPThematicArea extends Auditable<String> {
	private int id;
	private User fip;
	private ThematicArea thematicArea;
	private Integer experience;
	private String counterpart;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "fip_user_id", nullable = false)
	public User getFip() {
		return fip;
	}
	public void setFip(User fip) {
		this.fip = fip;
	}
	
	@ManyToOne
	@JoinColumn(name = "thematic_area_id", nullable = false)
	public ThematicArea getThematicArea() {
		return thematicArea;
	}
	public void setThematicArea(ThematicArea thematicArea) {
		this.thematicArea = thematicArea;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getCounterpart() {
		return counterpart;
	}

	public void setCounterpart(String counterpart) {
		this.counterpart = counterpart;
	}
}
