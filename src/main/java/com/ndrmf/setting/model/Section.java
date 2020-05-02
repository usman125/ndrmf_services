package com.ndrmf.setting.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.UUID;

@Entity
@Table(name = "sections",
		uniqueConstraints = @UniqueConstraint(columnNames = {"name", "process_type_id"}))
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Section extends Auditable<String> {
    private UUID id;
    private String name;
    private ProcessType processType;
    private User sme;
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
	
	@ManyToOne
	@JoinColumn(name="process_type_id", nullable = false)
	public ProcessType getProcessType() {
		return processType;
	}
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@ManyToOne
	@JoinColumn(name="sme_user_id")
	public User getSme() {
		return sme;
	}
	public void setSme(User sme) {
		this.sme = sme;
	}
}
