package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "qpr_to_donor")
public class QprToDonor extends Auditable<String> {
	private UUID id;
	private User initiatedBy;
	private String status;
	private List<QprToDonorSection> sections;
	private String generalComments;
//	private List<ProjectProposalAttachment> attachments;
	private Date created_at;
	
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
	@JoinColumn(name = "initiator_user_id")
	public User getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(User initiatedBy) {
		this.initiatedBy = initiatedBy;
	}


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@OneToMany(mappedBy="qprtodonorRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<QprToDonorSection> getSections() {
		return sections;
	}
	
	public void setSections(List<QprToDonorSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(QprToDonorSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<QprToDonorSection>();
		}
		
		section.setQprtodonorRef(this);
		
		this.sections.add(section);
	}

	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getGeneralComments() {
		return generalComments;
	}
	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}
	
//	@OneToMany(mappedBy="proposalRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
//	public List<ProjectProposalAttachment> getAttachments() {
//		return attachments;
//	}
//	public void setAttachments(List<ProjectProposalAttachment> attachments) {
//		this.attachments = attachments;
//	}
//
//	public void addAttachement(ProjectProposalAttachment a) {
//		if(this.attachments == null) {
//			this.attachments = new ArrayList<>();
//		}
//
//		a.setProposalRef(this);
//
//		this.attachments.add(a);
//	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
