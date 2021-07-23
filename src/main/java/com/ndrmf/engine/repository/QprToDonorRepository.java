package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.QprToDonor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QprToDonorRepository extends JpaRepository<QprToDonor, UUID>{
	@Query(value = "SELECT DISTINCT p FROM QprToDonor p "
			+ "JOIN p.initiatedBy ib "
			+ "JOIN p.sections ps "
			+ "JOIN ps.sme pssme "
			+ "WHERE ib.id = :userId  "
			+ "OR pssme.id = :userId")
	List<QprToDonor> findRequestsForInitiatorOrSME(
			@Param("userId") UUID userId
	);
	
	@Query(value = "SELECT DISTINCT p FROM QprToDonor p "
			+ "JOIN p.initiatedBy ib "
			+ "JOIN p.sections ps "
			+ "JOIN ps.sme pssme "
			+ "WHERE ib.id = :userId "
			+ "OR pssme.id = :userId")
	List<QprToDonor> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(
			@Param("userId") UUID userId
	);
	
	@Query(value = "SELECT p FROM QprToDonor p WHERE p.status = :status")
	List<QprToDonor> findAllRequestsByStatus(
			@Param("status") String status
	);

}
