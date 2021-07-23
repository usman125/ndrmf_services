package com.ndrmf.engine.dto;


import javax.validation.constraints.NotBlank;




public class AddCostHeadRequest {
	private String name;
	private String glCode;
	
	private String data;
	
	@NotBlank
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
