package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.QuarterlyProgressReport;
import com.ndrmf.engine.model.QuarterlyProgressReportSectionReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuarterlyProgressReportSectionReviewRepository extends JpaRepository<QuarterlyProgressReportSectionReview, UUID> {
	
//	@Query(value = "SELECT qpr FROM QuarterlyProgressReportSectionReview qpr "
//			+ "JOIN qpr.proposalRef p "
//			+ "JOIN p.initiatedBy u "
//			+ "WHERE u.id = :userId")
//	List<QuarterlyProgressReport> getQuarterlyProgressReportsForFIP(@Param("userId") UUID userId);
//
//	@Query(value = "SELECT qpr FROM QuarterlyProgressReportSectionReview qpr "
//			+ "JOIN qpr.proposalRef p "
//			+ "JOIN p.initiatedBy u "
//			+ "WHERE p.id = :proposalId")
//	List<QuarterlyProgressReport> getQuarterlyProgressReportsByProposalId(@Param("userId") UUID proposalId);
}
