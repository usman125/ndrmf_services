package com.ndrmf.complaint.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.complaint.model.ComplainantReview;
import com.ndrmf.complaint.model.ComplaintAssignee;

public interface ComplainantReviewRepository extends JpaRepository<ComplainantReview, UUID> {

	@Query("SELECT t FROM ComplainantReview t JOIN t.complaintRef r WHERE r.id=:complaintId ")
	List<ComplainantReview> getComplainantReviewByComplaintId(@Param("complaintId") UUID complaintId);

	
}
