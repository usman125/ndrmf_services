package com.ndrmf.complaint.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.complaint.model.ComplaintAssignee;
import com.ndrmf.complaint.model.ComplaintReview;

public interface ComplaintReviewRepository extends JpaRepository<ComplaintReview, UUID> {

	@Query("SELECT t FROM ComplaintReview t JOIN t.complaintRef r WHERE r.id=:complaintId ")
	List<ComplaintReview> getComplaintReviewByComplaintId(@Param("complaintId") UUID complaintId);

	
}
