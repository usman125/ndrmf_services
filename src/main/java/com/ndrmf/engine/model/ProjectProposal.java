package com.ndrmf.engine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.ThematicArea;
import com.ndrmf.user.model.User;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.springframework.data.annotation.CreatedDate;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "project_proposals")
public class ProjectProposal extends Auditable<String> {
	private UUID id;
	private String name;
	private String type;
	private ThematicArea thematicArea;
	private User initiatedBy;
	private User jvUser;
	private User processOwner;
	private String status;
	private List<ProjectProposalSection> sections;
	private PreliminaryAppraisal preAppraisal;
	private ExtendedAppraisal extendedAppraisal;
	private String generalComments;
	private List<ProjectProposalAttachment> attachments;
	private ProjectImplementationPlan pip;
	private GrantImplementationAgreement gia;
	private GIAChecklist giaChecklist;
	private List<SubProjectDocument> subProjectDocuments;
	private Integer numOfQuarters;
	private OfferLetter offerLetter;
	private GrantDisbursment grantDisbursment;
	private Date created_at;
	private String closed;
	private Tpv tpv;
	private ProjectClosure pc;
	
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
	
	@ManyToOne
	@JoinColumn(name = "owner_user_id")
	public User getProcessOwner() {
		return processOwner;
	}
	public void setProcessOwner(User processOwner) {
		this.processOwner = processOwner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@OneToMany(mappedBy="proposalRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ProjectProposalSection> getSections() {
		return sections;
	}
	
	public void setSections(List<ProjectProposalSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(ProjectProposalSection section) {
		if(this.sections == null) {
			this.sections = new ArrayList<ProjectProposalSection>();
		}
		
		section.setProposalRef(this);
		
		this.sections.add(section);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name = "thematic_area_id")
	public ThematicArea getThematicArea() {
		return thematicArea;
	}
	public void setThematicArea(ThematicArea thematicArea) {
		this.thematicArea = thematicArea;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pre_appraisal_id")
	public PreliminaryAppraisal getPreAppraisal() {
		return preAppraisal;
	}
	public void setPreAppraisal(PreliminaryAppraisal preAppraisal) {
		this.preAppraisal = preAppraisal;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ext_appraisal_id")
	public ExtendedAppraisal getExtendedAppraisal() {
		return extendedAppraisal;
	}
	public void setExtendedAppraisal(ExtendedAppraisal extendedAppraisal) {
		this.extendedAppraisal = extendedAppraisal;
	}
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String getGeneralComments() {
		return generalComments;
	}
	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}
	
	@OneToMany(mappedBy="proposalRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ProjectProposalAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<ProjectProposalAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public void addAttachement(ProjectProposalAttachment a) {
		if(this.attachments == null) {
			this.attachments = new ArrayList<>();
		}
		
		a.setProposalRef(this);
		
		this.attachments.add(a);
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pip_id")
	public ProjectImplementationPlan getPip() {
		return pip;
	}
	public void setPip(ProjectImplementationPlan pip) {
		this.pip = pip;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gia_id")
	public GrantImplementationAgreement getGia() {
		return gia;
	}
	public void setGia(GrantImplementationAgreement gia) {
		this.gia = gia;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tpv_id")
	public Tpv getTpv() {
		return tpv;
	}
	public void setTpv(Tpv tpv) {
		this.tpv = tpv;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pc_id")
	public ProjectClosure getPc() {
		return pc;
	}
	public void setPc(ProjectClosure pc) {
		this.pc = pc;
	}

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gia_checklist_id")
	public GIAChecklist getGiaChecklist() {
		return giaChecklist;
	}
	public void setGiaChecklist(GIAChecklist giaChecklist) {
		this.giaChecklist = giaChecklist;
	}
	
	@OneToMany(mappedBy="proposalRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<SubProjectDocument> getSubProjectDocuments() {
		return subProjectDocuments;
	}
	public void setSubProjectDocuments(List<SubProjectDocument> subProjectDocuments) {
		this.subProjectDocuments = subProjectDocuments;
	}
	
	public void addSubProjectDocument(SubProjectDocument doc) {
		if(this.subProjectDocuments == null) {
			this.subProjectDocuments = new ArrayList<>();
		}
		
		doc.setProposalRef(this);
		
		this.subProjectDocuments.add(doc);
	}
	@ManyToOne
	@JoinColumn(name = "jv_user_id")
	public User getJvUser() {
		return jvUser;
	}
	public void setJvUser(User jvUser) {
		this.jvUser = jvUser;
	}
	public Integer getNumOfQuarters() {
		return numOfQuarters;
	}
	public void setNumOfQuarters(Integer numOfQuarters) {
		this.numOfQuarters = numOfQuarters;
	}
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "offer_letter_id")
	public OfferLetter getOfferLetter() {
		return offerLetter;
	}
	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
	}
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grant_disb_id")
	public GrantDisbursment getGrantDisbursment() {
		return grantDisbursment;
	}
	public void setGrantDisbursment(GrantDisbursment grantDisbursment) {
		this.grantDisbursment = grantDisbursment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}
}
