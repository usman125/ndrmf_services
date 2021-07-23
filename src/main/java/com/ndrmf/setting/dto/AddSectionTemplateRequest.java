package com.ndrmf.setting.dto;

import javax.validation.constraints.NotBlank;

public class AddSectionTemplateRequest {
	private Integer totalScore;
	private Integer passingScore;
	private String templateType;
	private String template;
	private boolean enableAndEffective;
	
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getPassingScore() {
		return passingScore;
	}
	public void setPassingScore(Integer passingScore) {
		this.passingScore = passingScore;
	}
	
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	@NotBlank(message = "Template cannot be null or blank")
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public boolean isEnableAndEffective() {
		return enableAndEffective;
	}
	public void setEnableAndEffective(boolean enableAndEffective) {
		this.enableAndEffective = enableAndEffective;
	}
}
