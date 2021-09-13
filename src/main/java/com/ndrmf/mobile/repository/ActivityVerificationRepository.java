package com.ndrmf.mobile.repository;
import com.ndrmf.mobile.model.ActivityVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ActivityVerificationRepository extends JpaRepository<ActivityVerification, UUID>{
	@Query(value = "SELECT av FROM ActivityVerification av "
			+ "WHERE av.activityId = :activityId AND av.quarter = :quarter")
    	List<ActivityVerification> findRequestsForActivtyAndQuarter(
			@Param("activityId") String activityId, @Param("quarter") int quarter
	);

	@Query(value = "SELECT av FROM ActivityVerification av "
			+ "WHERE av.projectProposalRef.id = :proposalId")
		List<ActivityVerification> findRequestsForProposal(
			@Param("proposalId") UUID proposalId
	);
}
