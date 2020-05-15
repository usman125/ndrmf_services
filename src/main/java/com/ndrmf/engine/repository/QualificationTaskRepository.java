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
}
