package com.ndrmf.complaint.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.complaint.model.ComplaintAssignee;

public interface ComplaintAssigneeRepository extends JpaRepository<ComplaintAssignee, UUID> {

	@Query("SELECT t FROM ComplaintAssignee t JOIN t.complaintRef r WHERE r.id=:complaintId ")
	List<ComplaintAssignee> getComplaintAssigneeByComplaintId(@Param("complaintId") UUID complaintId);
}
