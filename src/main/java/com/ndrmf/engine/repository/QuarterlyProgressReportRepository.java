package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.QuarterlyProgressReport;

public interface QuarterlyProgressReportRepository extends JpaRepository<QuarterlyProgressReport, UUID> {
	
	@Query(value = "SELECT qpr FROM QuarterlyProgressReport qpr "
			+ "JOIN qpr.proposalRef p "
			+ "JOIN p.initiatedBy u "
			+ "JOIN qpr.processOwner qprpo "
			+ "WHERE u.id = :userId OR qprpo.id = :userId")
	List<QuarterlyProgressReport> getQuarterlyProgressReportsForFIP(@Param("userId") UUID userId);

	@Query(value = "SELECT qpr FROM QuarterlyProgressReport qpr "
			+ "JOIN qpr.proposalRef p "
			+ "JOIN p.initiatedBy u "
			+ "WHERE p.id = :proposalId")
	List<QuarterlyProgressReport> getQuarterlyProgressReportsByProposalId(@Param("proposalId") UUID proposalId);
}
