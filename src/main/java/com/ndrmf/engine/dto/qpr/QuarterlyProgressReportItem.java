package com.ndrmf.engine.dto.qpr;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ndrmf.engine.dto.SectionItem;
import com.ndrmf.notification.dto.TaskItem;

public class QuarterlyProgressReportItem {
	private String fipName;
	private String processOwnerName;
	private String status;
	private String subStatus;
	private LocalDate submittedAt;
	private LocalDate dueDate;
	private int quarter;
	private List<SectionItem> sections;
	private TaskItem reassignmentTask;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public LocalDate getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDate submittedAt) {
		this.submittedAt = submittedAt;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public List<SectionItem> getSections() {
		return sections;
	}

	public void setSections(List<SectionItem> sections) {
		this.sections = sections;
	}
	
	public void addSection(SectionItem section) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		this.sections.add(section);
	}

	public String getFipName() {
		return fipName;
	}

	public void setFipName(String fipName) {
		this.fipName = fipName;
	}

	public String getProcessOwnerName() {
		return processOwnerName;
	}

	public void setProcessOwnerName(String processOwnerName) {
		this.processOwnerName = processOwnerName;
	}

	public TaskItem getReassignmentTask() {
		return reassignmentTask;
	}

	public void setReassignmentTask(TaskItem reassignmentTask) {
		this.reassignmentTask = reassignmentTask;
	}
}
