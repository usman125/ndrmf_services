package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.QualificationTask;

public interface QualificationTaskRepository extends JpaRepository<QualificationTask, UUID>{
	@Query(value = "SELECT t FROM QualificationTask t JOIN t.assignee u WHERE u.id = :userId")
	List<QualificationTask> findAllTasksForAssignee(@Param("userId") UUID userId);
	
	@Query(value = "SELECT t FROM QualificationTask t JOIN t.assignee u JOIN t.qualification q WHERE u.id = :userId AND q.id = :qualificationId")
	List<QualificationTask> findAllTasksForAssigneeAndRequest(@Param("userId") UUID userId, @Param("qualificationId") UUID qualificationId);
	
	@Query(value = "SELECT t FROM QualificationTask t JOIN t.assignee u JOIN t.section s WHERE u.id = :userId AND s.id = :sectionId")
	List<QualificationTask> findTasksForSectionAndAssignee(@Param("userId") UUID userId, @Param("sectionId") UUID sectionId);
}
