package com.ndrmf.integration.dto;
import com.fasterxml.jackson.annotation.JsonAlias;

public class SAPUserItem {
	private String username, firstName, lastName, email, company;

	public String getUsername() {
		return username;
	}
	
	@JsonAlias("UserId")
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	@JsonAlias("Firstname")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@JsonAlias("Lastname")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	@JsonAlias("EMail")
	public void setEmail(String email) {
		if(email != null) {
			this.email = email.toLowerCase();
		}
	}
	
	public String getCompany() {
		return company;
	}
	
	@JsonAlias("Company")
	public void setCompany(String company) {
		this.company = company;
	}
}
