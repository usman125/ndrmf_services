package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.InitialAdvance;
import com.ndrmf.engine.model.ProjectProposal;

public interface InitialAdvanceRepository  extends JpaRepository<InitialAdvance, UUID>{
	
	@Query(value = "SELECT ia FROM InitialAdvance ia WHERE ia.status = :status")
	List<ProjectProposal> findAllRequestsByStatus(@Param("status") String status);

}
