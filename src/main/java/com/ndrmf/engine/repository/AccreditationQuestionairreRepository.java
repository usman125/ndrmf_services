package com.ndrmf.engine.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.AccreditationQuestionairre;

public interface AccreditationQuestionairreRepository extends JpaRepository<AccreditationQuestionairre, UUID>{
	@Query(value = "SELECT q FROM AccreditationQuestionairre q JOIN q.assignee a WHERE a.id = :userId AND q.status = :status")
	List<AccreditationQuestionairre> findAllByAssigneeAndStatus(@Param("userId") UUID userId, @Param("status") String status);
	
	@Query(value = "SELECT q FROM AccreditationQuestionairre q JOIN q.forUser a WHERE a.id = :userId")
	Optional<AccreditationQuestionairre> findByForUser(@Param("userId") UUID userId);
	
	@Query(value = "SELECT q FROM AccreditationQuestionairre q JOIN q.assignee a WHERE a.id = :userId")
	List<AccreditationQuestionairre> findAllByAssignee(@Param("userId") UUID userId);
}
