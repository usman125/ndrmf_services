package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.GrantDisbursmentAdvanceLiquidation;
import com.ndrmf.engine.model.GrantDisbursmentAdvanceReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface GrantDisbursmentAdvanceLiquidationRepository extends JpaRepository<GrantDisbursmentAdvanceLiquidation, UUID> {
    @Query(value = "SELECT gdal FROM GrantDisbursmentAdvanceLiquidation gdal WHERE gdal.initialAdvanceRef.id = :id")
    Optional<GrantDisbursmentAdvanceLiquidation> findRequestsByInitialAdvanceId(@Param("id") UUID id);
    @Query(value = "SELECT gdal FROM GrantDisbursmentAdvanceLiquidation gdal WHERE gdal.quarterAdvanceRef.id = :id")
    Optional<GrantDisbursmentAdvanceLiquidation> findRequestsByQuarterAdvanceId(@Param("id") UUID id);
}
