package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.GrantDisbursmentAdvanceLiquidation;
import com.ndrmf.engine.model.GrantDisbursmentAdvanceLiquidationSoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface GrantDisbursmentAdvanceLiquidationSoesRepository extends JpaRepository<GrantDisbursmentAdvanceLiquidationSoes, UUID> {
    @Query(value = "SELECT gdals FROM GrantDisbursmentAdvanceLiquidationSoes gdals WHERE gdals.initialAdvanceRef.id = :id")
    Optional<GrantDisbursmentAdvanceLiquidation> findRequestsByInitialAdvanceId(@Param("id") UUID id);
    @Query(value = "SELECT gdals FROM GrantDisbursmentAdvanceLiquidationSoes gdals WHERE gdals.quarterAdvanceRef.id = :id")
    Optional<GrantDisbursmentAdvanceLiquidation> findRequestsByQuarterAdvanceId(@Param("id") UUID id);
}
