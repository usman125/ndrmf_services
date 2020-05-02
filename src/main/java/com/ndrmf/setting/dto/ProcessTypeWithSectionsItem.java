package com.ndrmf.setting.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ndrmf.user.dto.UserLookupItem;

public class ProcessTypeWithSectionsItem {
	private UserLookupItem processOwner;
	private List<SectionItem> sections;

	public List<SectionItem> getSections() {
		return sections;
	}

	public void setSections(List<SectionItem> sections) {
		this.sections = sections;
	}
	
	public void addSection(UUID id, String name, boolean enabled) {
		if(this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		SectionItem s = new SectionItem();
		s.setId(id);
		s.setName(name);
		s.setEnabled(enabled);
		
		this.sections.add(s);
	}

	public UserLookupItem getProcessOwner() {
		return processOwner;
	}

	public void setProcessOwner(UserLookupItem processOwner) {
		this.processOwner = processOwner;
	}

	public static class SectionItem{
		private UUID id;
		private String name;
		private UserLookupItem sme;
		private boolean enabled;
		
		public UUID getId() {
			return id;
		}
		public void setId(UUID uuid) {
			this.id = uuid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public UserLookupItem getSme() {
			return sme;
		}
		public void setSme(UserLookupItem sme) {
			this.sme = sme;
		}
	}
}
