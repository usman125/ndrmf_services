package com.ndrmf.engine.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;




@Entity
@Table(name = "special_instrucitons")
public class SpecialInstructions {
	private UUID Id;
	private String instructions;
	//private List<Image> referenceImageUploads; TBD
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

}
