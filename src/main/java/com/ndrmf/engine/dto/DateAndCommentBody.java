package com.ndrmf.engine.dto;

import java.util.Date;
import java.time.LocalDate;

public class DateAndCommentBody {

	private LocalDate dueDate;

	private String comments;

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
