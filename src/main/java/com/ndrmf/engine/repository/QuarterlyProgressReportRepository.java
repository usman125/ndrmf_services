package com.ndrmf.engine.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.QuarterlyProgressReport;

public interface QuarterlyProgressReportRepository extends JpaRepository<QuarterlyProgressReport, UUID> {

}
