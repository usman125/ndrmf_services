package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.SubProjectDocument;

public interface SubProjectDocumentRepository extends JpaRepository<SubProjectDocument, UUID> {
	@Query(value = "SELECT spd FROM SubProjectDocument spd "
			+ "JOIN spd.proposalRef p "
			+ "JOIN p.initiatedBy u "
			+ "WHERE u.id = :userId AND spd.status = :status")
	List<SubProjectDocument> getSubProjectsForFIPByStatus(@Param("userId") UUID userId, @Param("status") String status);
}
