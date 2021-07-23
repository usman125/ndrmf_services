package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.GrantDisbursmentWithdrawalFiles;

import com.ndrmf.engine.model.GrantImplementationAgreementReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GrantDisbursmentWithdrawalFilesRepository extends JpaRepository<GrantDisbursmentWithdrawalFiles, UUID> {
    @Query(value = "SELECT gdwf FROM GrantDisbursmentWithdrawalFiles gdwf " +
            "WHERE gdwf.qaRef.id = :advanceId")
    List<GrantDisbursmentWithdrawalFiles> findRequestsByQuarterAdvanceId(@Param("advanceId") UUID advanceId);
}
