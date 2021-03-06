package com.ndrmf.engine.repository;
import com.ndrmf.engine.model.QuarterlyProgressReportTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuarterlyProgressReportTaskRepository extends JpaRepository<QuarterlyProgressReportTask, UUID> {

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.qpr qpr WHERE qpr.id = :qprId")
    List<QuarterlyProgressReportTask> findTasksForQpr(@Param("qprId") UUID qprId);

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.assignee u WHERE u.id = :userId")
    List<QuarterlyProgressReportTask> findAllTasksForAssignee(@Param("userId") UUID userId);

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.assignee u JOIN t.qpr qpr WHERE u.id = :userId AND qpr.id = :qprId")
    List<QuarterlyProgressReportTask> findAllTasksForAssigneeAndRequest(@Param("userId") UUID userId, @Param("qprId") UUID qprId);

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.assignee u JOIN t.section s WHERE u.id = :userId AND s.id = :sectionId")
    List<QuarterlyProgressReportTask> findTasksForSectionAndAssignee(@Param("userId") UUID userId, @Param("sectionId") UUID sectionId);

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.section s WHERE s.id = :sectionId ORDER BY t.createdDate DESC")
    List<QuarterlyProgressReportTask> findTasksForSection(@Param("sectionId") UUID sectionId, Pageable pageable);

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.qpr qpr WHERE qpr.id = :qprId AND t.section = null")
    List<QuarterlyProgressReportTask> findTasksForQprWithNoSection(@Param("qprId") UUID qprId);

    @Query(value = "SELECT t FROM QuarterlyProgressReportTask t JOIN t.assignee u JOIN t.qpr qpr WHERE u.id = :userId AND t.section = null AND qpr.id = :qprId")
    Optional<QuarterlyProgressReportTask> findTasksForUserWithNoSection(@Param("userId") UUID userId, @Param("qprId") UUID qprId);


}
