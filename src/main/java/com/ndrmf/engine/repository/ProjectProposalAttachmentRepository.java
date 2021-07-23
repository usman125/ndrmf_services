package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import com.ndrmf.engine.dto.ProjectProposalAttachmentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.ndrmf.engine.model.ProjectProposalAttachment;


public interface ProjectProposalAttachmentRepository extends JpaRepository<ProjectProposalAttachment, UUID> {
	@Query(value="SELECT ppafr.id, ppafr.fileName, ppafr.path, ppafr.data FROM ProjectProposalAttachment ppa "
			+"JOIN ppa.proposalRef ppar JOIN ppa.fileRef ppafr WHERE ppar.id = :proposalId AND ppa.stage = :stage")
			//+"WHERE ppa.proposal_id = :proposalId AND ppa.stage = :stage")
	List<Object>findAttachedFileIdsByProposalIdAndStage(@Param("proposalId") UUID proposalId, @Param("stage") String stage);
	
	
	@Query(value="SELECT ppa FROM ProjectProposalAttachment ppa "
			+"JOIN ppa.proposalRef ppar JOIN ppa.fileRef ppafr WHERE ppar.id = :proposalId")
	List<ProjectProposalAttachment>findAttachedFileIdsByProposalId(@Param("proposalId") UUID proposalId);

	@Query(value="SELECT ppafr.fileName, ppafr.path, ppa.stage, ppafr.data FROM ProjectProposalAttachment ppa "
			+"JOIN ppa.proposalRef ppar JOIN ppa.fileRef ppafr WHERE ppafr.fileName = :fileName AND ppafr.path = :filePath")
	List<ProjectProposalAttachment>findAttachedFileByfileNameAndPath(@Param("fileName") String fileName, @Param("filePath") String filePath);

	@Query(value="SELECT p FROM ProjectProposalAttachment p "
			+"WHERE p.proposalRef = :proposalId AND p.stage = :stage")
	List<ProjectProposalAttachment>findProjectProposalAttachmentByProposalIdAndStage(@Param("proposalId") UUID proposalId, @Param("stage") String stage);

}
