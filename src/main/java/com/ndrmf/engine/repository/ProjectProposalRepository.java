package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.ProjectProposal;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, UUID>{
	@Query(value = "SELECT p FROM ProjectProposal p "
			+ "JOIN p.processOwner po "
			+ "JOIN p.initiatedBy ib "
			+ "LEFT JOIN p.preAppraisal pa "
			+ "LEFT JOIN pa.assignee assig "
			+ "LEFT JOIN p.extendedAppraisal ea "
			+ "LEFT JOIN ea.sections eas "
			+ "LEFT JOIN eas.sme eassme "
			+ "WHERE (po.id = :userId OR ib.id = :userId OR assig.id = :userId OR eassme.id = :userId) "
			+ "AND p.status = :status")
	List<ProjectProposal> findRequestsForOwnerOrInitiatorOrDMPAMOrSMEByStatus(@Param("userId") UUID userId, @Param("status") String status);
	
	@Query(value = "SELECT p FROM ProjectProposal p "
			+ "JOIN p.processOwner po "
			+ "JOIN p.initiatedBy ib "
			+ "LEFT JOIN p.preAppraisal pa "
			+ "LEFT JOIN pa.assignee assig "
			+ "LEFT JOIN p.extendedAppraisal ea "
			+ "LEFT JOIN ea.sections eas "
			+ "LEFT JOIN eas.sme eassme "
			+ "WHERE po.id = :userId OR ib.id = :userId OR assig.id = :userId OR eassme.id = :userId")
	List<ProjectProposal> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(@Param("userId") UUID userId);
}
