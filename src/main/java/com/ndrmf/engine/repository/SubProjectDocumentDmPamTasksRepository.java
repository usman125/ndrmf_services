package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.SubProjectDocumentDmPamTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubProjectDocumentDmPamTasksRepository extends JpaRepository<SubProjectDocumentDmPamTasks, UUID> {
	@Query(value = "SELECT spddmt FROM SubProjectDocumentDmPamTasks spddmt "
			+ "JOIN spddmt.assignee u "
			+ "WHERE u.id = :userId")
	List<SubProjectDocumentDmPamTasks> getSubProjectDocumentDmPamTasksByUserId(@Param("userId") UUID userId);

	@Query(value = "SELECT spddmt FROM SubProjectDocumentDmPamTasks spddmt "
			+ "JOIN spddmt.subProjectRef spdref "
			+ "WHERE spdref.id = :requestId")
	List<SubProjectDocumentDmPamTasks> getSubProjectDocumentDmPamTasksBySpdId(@Param("requestId") UUID requestId);
}
