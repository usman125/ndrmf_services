package com.ndrmf.engine.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

public class AddQprSectionReviewRequest {

	private String comments;

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}
