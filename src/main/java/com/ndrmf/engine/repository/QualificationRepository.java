package com.ndrmf.engine.repository;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.Qualification;

public interface QualificationRepository extends JpaRepository<Qualification, UUID>{
	@Query(value = "SELECT COUNT(q) FROM Qualification q JOIN q.initiatedBy ib WHERE ib.id = :userId AND q.status IN (:statuses)")
	int checkCountForUserWithStatuses(@Param("userId") UUID userId, @Param("statuses") Set<String> statuses);
}
