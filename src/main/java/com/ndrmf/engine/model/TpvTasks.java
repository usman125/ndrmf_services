package com.ndrmf.engine.model;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.List;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "tpv_tasks")
public class TpvTasks extends Auditable<String> {
	private UUID id;
	private User initiatedBy;
	private String status;
	private String generalFields;
	private Date created_at;
	private Tpv tpvRef;
	private Integer orderNum;
	private String generalComments;
	private String name;
	private List<TpvTasksFiles> files;

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

	public String getGeneralFields() {
		return generalFields;
	}
	public void setGeneralFields(String generalFields) {
		this.generalFields = generalFields;
	}

	@ManyToOne
	@JoinColumn(name = "tpv_id")
	public Tpv getTpvRef() {
		return tpvRef;
	}
	public void setTpvRef(Tpv tpvRef) {
		this.tpvRef = tpvRef;
	}

	@OneToMany(mappedBy="tpvTaskRef", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<TpvTasksFiles> getFiles() {
		return files;
	}
	public void setFiles(List<TpvTasksFiles> files) {
		this.files = files;
	}

	public void addFile(TpvTasksFiles file){
		if (this.files == null){
			this.files = new ArrayList<>();
		}

		file.setTpvTaskRef(this);
		this.files.add(file);
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

	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer order) {
		this.orderNum = order;
	}

	public String getGeneralComments() {
		return generalComments;
	}

	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
