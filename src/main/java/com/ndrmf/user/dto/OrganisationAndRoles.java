package com.ndrmf.user.dto;

import java.util.ArrayList;
import java.util.List;

public class OrganisationAndRoles {
	private int id;
	private String name;
	private List<Role> roles;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(int id, String name) {
		Role r = new Role();
		r.setId(id);
		r.setName(name);
		
		if(this.roles == null) {
			this.roles = new ArrayList<>();
		}
		
		this.roles.add(r);
	}

	public static class Role{
		private int id;
		private String name;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
