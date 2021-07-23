package com.ndrmf.engine.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "project_activity_financials")
public class ProjectActivityQuarterFinancial {
	private long id;
	private int quarter;
	private double ndrmfShare;
	private double fipShare;
	private double ndrmfShareSpent;
	private double fipShareSpent;
	private ProjectActivity activity;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public double getNdrmfShare() {
		return ndrmfShare;
	}
	public void setNdrmfShare(double ndrmfShare) {
		this.ndrmfShare = ndrmfShare;
	}
	public double getFipShare() {
		return fipShare;
	}
	public void setFipShare(double fipShare) {
		this.fipShare = fipShare;
	}
	public double getNdrmfShareSpent() {
		return ndrmfShareSpent;
	}
	public void setNdrmfShareSpent(double ndrmfShareSpent) {
		this.ndrmfShareSpent = ndrmfShareSpent;
	}
	public double getFipShareSpent() {
		return fipShareSpent;
	}
	public void setFipShareSpent(double fipShareSpent) {
		this.fipShareSpent = fipShareSpent;
	}
	
	@ManyToOne
	@JoinColumn(name = "activity_id")
	public ProjectActivity getActivity() {
		return activity;
	}
	public void setActivity(ProjectActivity activity) {
		this.activity = activity;
	}
}
