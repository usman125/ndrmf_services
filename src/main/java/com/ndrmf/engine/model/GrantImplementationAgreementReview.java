package com.ndrmf.engine.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ndrmf.config.audit.Auditable;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.ndrmf.user.model.User;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "grant_implementation_agreement_reviews")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class GrantImplementationAgreementReview extends Auditable<String>{
	private UUID id;
	private User assignee;
	private String status;
	private String comments;
	private String poComments;
	private Date startDate;
	private Date endDate;
	private GrantImplementationAgreement giaRef;
	private ProjectProposal proposalRef;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "assignee_user_id", nullable = false)
	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name = "gia_id", nullable = false)
	public GrantImplementationAgreement getGiaRef() {
		return giaRef;
	}
	public void setGiaRef(GrantImplementationAgreement giaRef) {
		this.giaRef = giaRef;
	}

	@ManyToOne
	@JoinColumn(name = "proposal_ref", nullable = false)
	public ProjectProposal getProposalRef(){return proposalRef;}
	public void setProposalRef(ProjectProposal proposalRef){this.proposalRef = proposalRef;}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPoComments() {
		return poComments;
	}

	public void setPoComments(String poComments) {
		this.poComments = poComments;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
