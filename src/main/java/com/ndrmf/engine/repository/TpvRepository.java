package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.Tpv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TpvRepository extends JpaRepository<Tpv, UUID>{
	@Query(value = "SELECT DISTINCT p FROM Tpv p "
			+ "JOIN p.projectProposalRef pcr "
			+ "WHERE pcr.id = :proposalId")
	List<Tpv> findRequestsForProjectPropsal(
			@Param("proposalId") UUID proposalId
	);
//
//	@Query(value = "SELECT DISTINCT p FROM Tpv p "
//			+ "JOIN p.initiatedBy ib "
//			+ "JOIN p.sections ps "
//			+ "JOIN ps.sme pssme "
//			+ "WHERE ib.id = :userId "
//			+ "OR pssme.id = :userId")
//	List<Tpv> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(
//			@Param("userId") UUID userId
//	);
//
//	@Query(value = "SELECT p FROM Tpv p WHERE p.status = :status")
//	List<Tpv> findAllRequestsByStatus(
//			@Param("status") String status
//	);

}
