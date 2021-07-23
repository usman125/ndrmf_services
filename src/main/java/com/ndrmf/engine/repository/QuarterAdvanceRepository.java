package com.ndrmf.engine.repository;

import com.ndrmf.engine.dto.QuarterAdvanceItem;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.QuarterAdvance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuarterAdvanceRepository extends JpaRepository<QuarterAdvance, UUID>{

//	@Query(value = "SELECT ia FROM InitialAdvance ia WHERE ia.grant_disbursment_id = :disbursmentId")
//	List<ProjectProposal> findAllRequestsByDisbursmentId(@Param("disbursmentId") UUID disbursmentId);

    @Query(value = "SELECT amount, data, id, status FROM QuarterAdvance qa " +
            "WHERE qa.grantDisbursmentRef.id = :disbursmentId")
	List<QuarterAdvance> findQuarterAdvanceByDisbursmentId(@Param("disbursmentId") UUID disbursmentId);

}
