package com.ndrmf.engine.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.ProjectActivityQuarterDetail;

public interface ProjectActivityQuarterDetailRepository  extends JpaRepository<ProjectActivityQuarterDetail, UUID> {

}
