package com.ndrmf.engine.repository;


import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.GrantDisbursment;
import com.ndrmf.engine.model.ProjectProposal;


public interface GrantDisbursmentRepository  extends JpaRepository<GrantDisbursment, UUID>{

//	@Query(value = "SELECT DISTINCT gd FROM GrantDisbursment gd "
//			+ "LEFT JOIN p.proposalRef pr "
//			+ "WHERE pr.initiatedBy.id = :userId "
//			+ "OR pr.processOwner.id = :userId")
//			+ "OR assig.id = :userId OR eassme.id = :userId OR pssme.id = :userId "
//			+ "OR giareviewassignee.id = :userId "
//			+ "OR pgiaassignee.id = :userId")
//	@Query(value = "SELECT g FROM GrantDisbursment g WHERE g.initAdvanceRequestStatus = :initAdvanceRequestStatus")
	@Query(value = "SELECT DISTINCT gd FROM GrantDisbursment gd "
			+ "LEFT JOIN gd.proposalRef gdpr "
			+ "LEFT JOIN gd.initAdvance gdia "
			+ "LEFT JOIN gdia.reviewsList gdiars "
			+ "LEFT JOIN gdiars.assignee gdiarsass "
			+ "LEFT JOIN gd.quarterAdvances gdqas "
			+ "LEFT JOIN gdqas.reviewsList gdqasrs "
			+ "LEFT JOIN gdqasrs.assignee gdqasrsass "
			+ "WHERE gdpr.initiatedBy.id = :userId "
			+ "OR gdpr.processOwner.id = :userId "
			+ "OR gdiarsass.id = :userId "
			+ "OR gdqasrsass.id = :userId "
			+ "OR gd.owner.id = :userId")
	List<GrantDisbursment> findAllRequestsByPoAndOwner(@Param("userId") UUID userId);
}
