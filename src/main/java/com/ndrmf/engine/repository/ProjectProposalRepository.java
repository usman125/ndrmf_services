package com.ndrmf.engine.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.engine.model.ProjectProposal;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, UUID>{
	@Query(value = "SELECT DISTINCT p FROM ProjectProposal p "
			+ "JOIN p.processOwner po "
			+ "JOIN p.initiatedBy ib "
			+ "JOIN p.sections ps "
			+ "JOIN ps.sme pssme "
			+ "LEFT JOIN p.preAppraisal pa "
			+ "LEFT JOIN pa.assignee assig "
			+ "LEFT JOIN p.extendedAppraisal ea "
			+ "LEFT JOIN ea.sections eas "
			+ "LEFT JOIN eas.sme eassme "
			+ "LEFT JOIN p.gia pgia "
			+ "LEFT JOIN pgia.reviews giareveiws "
			+ "LEFT JOIN giareveiws.assignee giareviewassignee "
			+ "WHERE (po.id = :userId OR ib.id = :userId OR assig.id = :userId "
			+ "OR eassme.id = :userId OR pssme.id = :userId "
			+ "OR giareviewassignee.id = :userId) "
			+ "AND p.status = :status")
	List<ProjectProposal> findRequestsForOwnerOrInitiatorOrDMPAMOrSMEByStatus(@Param("userId") UUID userId, @Param("status") String status);
	
	@Query(value = "SELECT DISTINCT p FROM ProjectProposal p "
			+ "JOIN p.processOwner po "
			+ "JOIN p.initiatedBy ib "
			+ "JOIN p.sections ps "
			+ "JOIN ps.sme pssme "
			+ "LEFT JOIN p.preAppraisal pa "
			+ "LEFT JOIN pa.assignee assig "
			+ "LEFT JOIN p.extendedAppraisal ea "
			+ "LEFT JOIN ea.sections eas "
			+ "LEFT JOIN eas.sme eassme "
			+ "LEFT JOIN p.gia pgia "
			+ "LEFT JOIN pgia.reviews giareveiws "
			+ "LEFT JOIN giareveiws.assignee giareviewassignee "
			+ "WHERE po.id = :userId OR ib.id = :userId "
			+ "OR assig.id = :userId OR eassme.id = :userId OR pssme.id = :userId "
			+ "OR giareviewassignee.id = :userId")
	List<ProjectProposal> findAllRequestsForOwnerOrInitiatorOrDMPAMOrSME(@Param("userId") UUID userId);
	
	@Query(value = "SELECT p FROM ProjectProposal p WHERE p.status = :status")
	List<ProjectProposal> findAllRequestsByStatus(@Param("status") String status);
}
