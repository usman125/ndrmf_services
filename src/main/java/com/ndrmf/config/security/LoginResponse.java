package com.ndrmf.config.security;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoginResponse {
	private String accessToken;
	private User user;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public static class User{
		private UUID id;
		private String username;
		private String email;
		private String firstName;
		private String lastName;
		private int orgId;
		private String orgName;
		private String[] roles;
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
	    public String getFullName() {
	    	if(this.lastName == null)
	    		return this.firstName;
	    	else
	    		return this.firstName + " " + this.lastName;
	    }
		
		public int getOrgId() {
			return orgId;
		}
		public void setOrgId(int orgId) {
			this.orgId = orgId;
		}
		public String getOrgName() {
			return orgName;
		}
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
		public String[] getRoles() {
			return roles;
		}
		public void setRoles(String[] roles) {
			this.roles = roles;
		}
		
//		@JsonIgnore
		public UUID getId() {
			return id;
		}
		public void setId(UUID id) {
			this.id = id;
		}
	}
}
