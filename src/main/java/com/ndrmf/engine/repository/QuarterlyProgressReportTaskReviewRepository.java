package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.QuarterlyProgressReportTask;
import com.ndrmf.engine.model.QuarterlyProgressReportTaskReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuarterlyProgressReportTaskReviewRepository extends JpaRepository<QuarterlyProgressReportTaskReview, UUID> {
	
//	@Query(value = "SELECT qpr FROM QuarterlyProgressReportTask qprt "
//			+ "JOIN qpr.proposalRef p "
//			+ "JOIN p.initiatedBy u "
//			+ "WHERE u.id = :userId")
//	List<QuarterlyProgressReport> getQuarterlyProgressReportsForFIP(@Param("userId") UUID userId);
//
//	@Query(value = "SELECT qpr FROM QuarterlyProgressReportTask qprt "
//			+ "JOIN qpr.proposalRef p "
//			+ "JOIN p.initiatedBy u "
//			+ "WHERE p.id = :proposalId")
//	List<QuarterlyProgressReport> getQuarterlyProgressReportsByProposalId(@Param("userId") UUID proposalId);
//    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.assignee u JOIN t.section s WHERE u.id = :userId AND s.id = :sectionId")
//    List<QuarterlyProgressReportTask> findTasksForSectionAndAssignee(@Param("userId") UUID userId, @Param("sectionId") UUID sectionId);
//
//    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.assignee u WHERE u.id = :userId")
//    List<QuarterlyProgressReportTask> findAllTasksForAssignee(@Param("userId") UUID userId);
//
    @Query(value = "SELECT t FROM QuarterlyProgressReportTaskReview t JOIN t.qprTaskRef qprt WHERE qprt.id = :taskId")
    Optional<QuarterlyProgressReportTaskReview> findReviewsForTask(@Param("taskId") UUID taskId);

//    @Query(value = "SELECT t FROM QuarterlyProgressReportTaskReview t JOIN t.qprTaskRef qprt WHERE qprt.id = :taskId")
//    Optional<QuarterlyProgressReportTaskReview> findTasksForQpr(@Param("taskId") UUID taskId);


}
