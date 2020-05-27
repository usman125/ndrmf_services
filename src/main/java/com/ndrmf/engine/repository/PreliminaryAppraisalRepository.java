package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.PreliminaryAppraisal;

public interface PreliminaryAppraisalRepository extends JpaRepository<PreliminaryAppraisal, UUID> {
	@Query(value = "SELECT a FROM PreliminaryAppraisal a JOIN a.assignee u WHERE u.id = :userId")
	List<PreliminaryAppraisal> findAllAppraisalsForAssignee(@Param("userId") UUID userId);
	
	@Query(value = "SELECT a FROM PreliminaryAppraisal a JOIN a.assignee u WHERE u.id = :userId AND a.status = :status")
	List<PreliminaryAppraisal> findAllAppraisalsForAssigneeAndStatus(@Param("userId") UUID userId, @Param("status") String status);
}
