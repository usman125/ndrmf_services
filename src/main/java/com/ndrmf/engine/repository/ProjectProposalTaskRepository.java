package com.ndrmf.engine.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.ProjectProposalTask;

public interface ProjectProposalTaskRepository extends JpaRepository<ProjectProposalTask, UUID>{
	@Query(value = "SELECT t FROM ProjectProposalTask t JOIN t.assignee u WHERE u.id = :userId")
	List<ProjectProposalTask> findAllTasksForAssignee(@Param("userId") UUID userId);
	
	@Query(value = "SELECT t FROM ProjectProposalTask t JOIN t.assignee u JOIN t.proposal p WHERE u.id = :userId AND p.id = :proposalId")
	List<ProjectProposalTask> findAllTasksForAssigneeAndRequest(@Param("userId") UUID userId, @Param("proposalId") UUID proposalId);
	
	@Query(value = "SELECT t FROM ProjectProposalTask t JOIN t.assignee u JOIN t.section s WHERE u.id = :userId AND s.id = :sectionId")
	List<ProjectProposalTask> findTasksForSectionAndAssignee(@Param("userId") UUID userId, @Param("sectionId") UUID sectionId);
	
	@Query(value = "SELECT t FROM ProjectProposalTask t JOIN t.section s WHERE s.id = :sectionId ORDER BY t.createdDate DESC")
	Optional<ProjectProposalTask> findLatestTaskForSection(@Param("sectionId") UUID sectionId);
}
