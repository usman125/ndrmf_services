package com.ndrmf.engine.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.Qualification;

public interface QualificationRepository extends JpaRepository<Qualification, UUID>{
	@Query(value = "SELECT COUNT(q) FROM Qualification q JOIN q.initiatedBy ib WHERE ib.id = :userId AND q.status IN (:statuses)")
	int checkCountForUserWithStatuses(@Param("userId") UUID userId, @Param("statuses") Set<String> statuses);
	
	@Query(value = "SELECT q FROM Qualification q JOIN q.processOwner po JOIN q.initiatedBy ib WHERE (po.id = :userId OR ib.id = :userId) AND q.status = :status")
	List<Qualification> findRequestsForOwnerOrInitiatorByStatus(@Param("userId") UUID userId, @Param("status") String status);
	
	@Query(value = "SELECT q FROM Qualification q JOIN q.processOwner po JOIN q.initiatedBy ib WHERE po.id = :userId OR ib.id = :userId")
	List<Qualification> findAllRequestsForOwnerOrInitiator(@Param("userId") UUID userId);
}