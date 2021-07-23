package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.SubProjectDocumentTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubProjectDocumentTasksRepository extends JpaRepository<SubProjectDocumentTasks, UUID> {
	@Query(value = "SELECT spddmt FROM SubProjectDocumentTasks spddmt "
			+ "JOIN spddmt.assignee u "
			+ "WHERE u.id = :userId")
	List<SubProjectDocumentTasks> getSubProjectDocumentTasksByUserId(@Param("userId") UUID userId);

	@Query(value = "SELECT spdt FROM SubProjectDocumentTasks spdt "
			+ "JOIN spdt.subProjectDocumentDmPamTasksRef spdtdmpref "
			+ "WHERE spdtdmpref.id = :requestId")
	List<SubProjectDocumentTasks> getSubProjectDocumentTasksBySpdDmpamId(@Param("requestId") UUID requestId);
}
