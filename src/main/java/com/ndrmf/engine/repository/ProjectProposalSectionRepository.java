package com.ndrmf.engine.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.ProjectProposalSection;

public interface ProjectProposalSectionRepository extends JpaRepository<ProjectProposalSection, UUID> {

}
