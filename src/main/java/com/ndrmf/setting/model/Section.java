package com.ndrmf.setting.model;

import com.ndrmf.config.audit.Auditable;

import javax.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "sections",
		uniqueConstraints = @UniqueConstraint(columnNames = {"name", "processType"}))
public class Section extends Auditable<String> {
    private UUID id;
    private String name;
    private String processType;
    private boolean enabled;
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable = false)
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
