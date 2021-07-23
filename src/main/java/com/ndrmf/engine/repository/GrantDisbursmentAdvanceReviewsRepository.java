package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.GrantDisbursment;
import com.ndrmf.engine.model.GrantDisbursmentAdvanceReviews;
import com.ndrmf.engine.model.ProjectProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GrantDisbursmentAdvanceReviewsRepository extends JpaRepository<GrantDisbursmentAdvanceReviews, UUID> {

    @Query(value = "SELECT gdar FROM GrantDisbursmentAdvanceReviews gdar WHERE gdar.initialAdvanceRef.id = :id")
    List<GrantDisbursmentAdvanceReviews> findAllRequestsByInitialAdvance(@Param("id") UUID id);

    @Query(value = "SELECT gdar FROM GrantDisbursmentAdvanceReviews gdar WHERE gdar.quarterAdvanceRef.id = :id")
    List<GrantDisbursmentAdvanceReviews> findAllRequestsByQuarterAdvance(@Param("id") UUID id);


    @Query(value = "SELECT gdar FROM GrantDisbursmentAdvanceReviews gdar WHERE gdar.id = :id")
    Optional<GrantDisbursmentAdvanceReviews> findRequestsById(@Param("id") UUID id);


}
