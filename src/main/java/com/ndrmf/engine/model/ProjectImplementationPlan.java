package com.ndrmf.engine.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ndrmf.config.audit.Auditable;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "project_implementation_plan")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ProjectImplementationPlan extends Auditable<String>{
	private UUID id;
	private ProjectProposal proposalRef;
	private String implementationPlan;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@OneToOne(mappedBy = "pip")
	public ProjectProposal getProposalRef() {
		return proposalRef;
	}
	public void setProposalRef(ProjectProposal proposalRef) {
		this.proposalRef = proposalRef;
	}
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getImplementationPlan() {
		return implementationPlan;
	}
	public void setImplementationPlan(String implementationPlan) {
		this.implementationPlan = implementationPlan;
	}
}
