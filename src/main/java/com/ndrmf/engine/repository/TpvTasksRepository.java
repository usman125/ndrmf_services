package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.TpvTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TpvTasksRepository extends JpaRepository<TpvTasks, UUID>{
    @Query(value = "SELECT p FROM TpvTasks p "
            + "JOIN p.tpvRef ptpv "
            + "JOIN ptpv.projectProposalRef ppr "
            + "WHERE ppr.id = :proposalId")
    List<TpvTasks> findRequestsForProjectPropsal(
            @Param("proposalId") UUID proposalId
    );
//
//	@Query(value = "SELECT DISTINCT p FROM TpvTasks p "
//			+ "JOIN p.initiatedBy ib "
//			+ "JOIN p.sections ps "
//			+ "JOIN ps.sme pssme "
//			+ "WHERE ib.id = :userId "
//			+ "OR pssme.id = :userId")
//	List<TpvTasks> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(
//			@Param("userId") UUID userId
//	);
//
//	@Query(value = "SELECT p FROM TpvTasks p WHERE p.status = :status")
//	List<TpvTasks> findAllRequestsByStatus(
//			@Param("status") String status
//	);

}
