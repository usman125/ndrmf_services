package com.ndrmf.engine.repository;
import java.util.UUID;
import java.util.List;

import com.ndrmf.engine.model.GrantImplementationAgreementReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GrantImplementationAgreementReviewRepository extends JpaRepository<GrantImplementationAgreementReview, UUID> {
    @Query(value = "SELECT giar FROM GrantImplementationAgreementReview giar " +
            "WHERE giar.proposalRef.id = :proposalId")
    List<GrantImplementationAgreementReview> findRequestsByProposalId(@Param("proposalId") UUID proposalId);
}
