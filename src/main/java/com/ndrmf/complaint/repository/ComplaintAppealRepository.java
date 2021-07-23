package com.ndrmf.complaint.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.complaint.model.ComplaintAppeal;
import com.ndrmf.complaint.model.ComplaintReview;

public interface ComplaintAppealRepository extends JpaRepository<ComplaintAppeal,UUID>{

	@Query("SELECT t FROM ComplaintAppeal t JOIN t.complaintRef r WHERE r.id=:complaintId ")
	List<ComplaintAppeal> getComplaintAppealByComplaintId(@Param("complaintId") UUID complaintId);
	List<ComplaintAppeal> findByStatusOrderByAppealDateTimeDesc(String status);
	
}
