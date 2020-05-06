package com.ndrmf.engine.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.Eligibility;

public interface EligibilityRepository extends JpaRepository<Eligibility, UUID> {
	
	@Query(value = "SELECT COUNT(e) FROM Eligibility e JOIN e.initiatedBy ib WHERE ib.username = :username AND e.status IN (:statuses)")
	int checkCountForUserWithStatuses(@Param("username") String username, @Param("statuses") Set<String> statuses);
	
	@Query(value = "SELECT e FROM Eligibility e JOIN e.processOwner po WHERE po.username = :username AND e.status = :status")
	List<Eligibility> findRequestsForOwnerByStatus(@Param("username") String username, @Param("status") String status);
	
	@Query(value = "SELECT e FROM Eligibility e JOIN e.processOwner po WHERE po.username = :username")
	List<Eligibility> findAllRequestsForOwner(@Param("username") String username);
}
