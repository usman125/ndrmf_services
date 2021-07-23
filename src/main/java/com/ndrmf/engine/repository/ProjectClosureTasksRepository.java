package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.ProjectClosure;
import com.ndrmf.engine.model.ProjectClosureTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectClosureTasksRepository extends JpaRepository<ProjectClosureTasks, UUID>{
    @Query(value = "SELECT p FROM ProjectClosureTasks p "
            + "JOIN p.projectClosureRef pcr "
            + "JOIN pcr.projectProposalRef ppr "
            + "WHERE ppr.id = :proposalId ORDER BY p.orderNum")
    List<ProjectClosureTasks> findRequestsForProjectPropsal(
            @Param("proposalId") UUID proposalId
    );
//
//	@Query(value = "SELECT DISTINCT p FROM ProjectClosureTasks p "
//			+ "JOIN p.initiatedBy ib "
//			+ "JOIN p.sections ps "
//			+ "JOIN ps.sme pssme "
//			+ "WHERE ib.id = :userId "
//			+ "OR pssme.id = :userId")
//	List<ProjectClosureTasks> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(
//			@Param("userId") UUID userId
//	);
//
//	@Query(value = "SELECT p FROM ProjectClosureTasks p WHERE p.status = :status")
//	List<ProjectClosureTasks> findAllRequestsByStatus(
//			@Param("status") String status
//	);

}
