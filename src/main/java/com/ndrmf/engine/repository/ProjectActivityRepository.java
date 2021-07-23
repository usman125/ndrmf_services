package com.ndrmf.engine.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.ProjectActivity;

public interface ProjectActivityRepository extends JpaRepository<ProjectActivity, UUID>{

}
