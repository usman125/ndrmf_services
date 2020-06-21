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
	
	@Query(value = "SELECT spd FROM SubProjectDocument spd "
			+ "JOIN spd.processOwner u "
			+ "LEFT JOIN spd.sections s "
			+ "LEFT JOIN s.sme ssme "
			+ "WHERE (u.id = :userId OR (ssme.id = :userId AND s.reviewStatus = 'Pending')) "
			+ "AND spd.status NOT IN (:excludedStatuses)")
	List<SubProjectDocument> getSubProjectsForPOOrReviewer(@Param("userId") UUID userId,
			@Param("excludedStatuses") List<String> excludedStatuses);
}
