package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.QprToDonorTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QprToDonorTaskRepository extends JpaRepository<QprToDonorTask, UUID>{
	@Query(value = "SELECT t FROM QprToDonorTask t JOIN t.assignee u WHERE u.id = :userId")
	List<QprToDonorTask> findAllTasksForAssignee(@Param("userId") UUID userId);

	@Query(value = "SELECT t FROM QprToDonorTask t JOIN t.assignee u JOIN t.qprtodonor p WHERE u.id = :userId AND p.id = :proposalId")
	List<QprToDonorTask> findAllTasksForAssigneeAndRequest(@Param("userId") UUID userId, @Param("proposalId") UUID proposalId);

	@Query(value = "SELECT t FROM QprToDonorTask t JOIN t.assignee u JOIN t.section s WHERE u.id = :userId AND s.id = :sectionId")
	List<QprToDonorTask> findTasksForSectionAndAssignee(@Param("userId") UUID userId, @Param("sectionId") UUID sectionId);

	@Query(value = "SELECT t FROM QprToDonorTask t JOIN t.section s WHERE s.id = :sectionId ORDER BY t.createdDate DESC")
	List<QprToDonorTask> findTasksForSection(@Param("sectionId") UUID sectionId, Pageable pageable);
}
