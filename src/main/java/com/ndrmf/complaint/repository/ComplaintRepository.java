package com.ndrmf.complaint.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.complaint.model.Complaint;
import com.ndrmf.util.enums.ComplaintPriorty;
import com.ndrmf.util.enums.ComplaintStatus;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

	List<Complaint> findByStatus(String status);
	List<Complaint> findByInternalStatus(String status);
	List<Complaint> findByPriority(String priority);

	@Query("SELECT t FROM Complaint t JOIN t.assignee s JOIN s.assignedPerson r WHERE r.id = :userId AND t.status= :status")
	List<Complaint> findComplaintByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") String status);

	
	  @Query
	  ("SELECT t FROM Complaint t JOIN t.assignee s JOIN s.assignedPerson r WHERE r.id = :userId ORDER BY t.complaintDateTime DESC" )
	  List<Complaint> findComplaintByUserId(@Param("userId") UUID userId);
	
	  @Query
	  ("SELECT t FROM Complaint t WHERE t.id = :id")
	   Complaint findComplaintById(@Param("id") UUID id);
	 

}
