package com.ndrmf.setting.dto;

import java.util.ArrayList;
import java.util.List;

public class ProcessTemplateItem {
	private String processType;
	private List<SectionTemplate> sections;
	
	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public List<SectionTemplate> getSections() {
		return sections;
	}

	public void setSections(List<SectionTemplate> sections) {
		this.sections = sections;
	}
	
	public void addSection(String sectionName, Integer totalScore, Integer passingScore, String templateType, String template) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		this.sections.add(new SectionTemplate(sectionName, totalScore, passingScore, templateType, template));
	}

	public static class SectionTemplate{
		private String sectionName;
		private Integer totalScore;
		private Integer passingScore;
		private String templateType;
		private String template;
		
		public SectionTemplate(String sectionName, Integer totalScore, Integer passingScore, String templateType,
				String template) {
			this.sectionName = sectionName;
			this.totalScore = totalScore;
			this.passingScore = passingScore;
			this.templateType = templateType;
			this.template = template;
		}
		
		public String getSectionName() {
			return sectionName;
		}
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
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
		public String getTemplate() {
			return template;
		}
		public void setTemplate(String template) {
			this.template = template;
		}
	}
}
