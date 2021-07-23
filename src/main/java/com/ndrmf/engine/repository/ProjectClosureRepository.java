package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.ProjectClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectClosureRepository extends JpaRepository<ProjectClosure, UUID>{
	@Query(value = "SELECT DISTINCT p FROM ProjectClosure p "
			+ "JOIN p.projectProposalRef pcr "
			+ "WHERE pcr.id = :proposalId")
	List<ProjectClosure> findRequestsForProjectPropsal(
			@Param("proposalId") UUID proposalId
	);
//
//	@Query(value = "SELECT DISTINCT p FROM ProjectClosure p "
//			+ "JOIN p.initiatedBy ib "
//			+ "JOIN p.sections ps "
//			+ "JOIN ps.sme pssme "
//			+ "WHERE ib.id = :userId "
//			+ "OR pssme.id = :userId")
//	List<ProjectClosure> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(
//			@Param("userId") UUID userId
//	);
//
//	@Query(value = "SELECT p FROM ProjectClosure p WHERE p.status = :status")
//	List<ProjectClosure> findAllRequestsByStatus(
//			@Param("status") String status
//	);

}
