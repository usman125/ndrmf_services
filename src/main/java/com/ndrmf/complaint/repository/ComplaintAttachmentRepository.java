package com.ndrmf.complaint.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.complaint.model.ComplaintAttachment;
import com.ndrmf.complaint.model.ComplaintReview;

public interface ComplaintAttachmentRepository extends JpaRepository<ComplaintAttachment, Long> {

	@Query("SELECT t FROM ComplaintAttachment t JOIN t.complaintRef r WHERE r.id=:complaintId ")
	List<ComplaintAttachment> getComplaintAttachmentByComplaintId(@Param("complaintId") UUID complaintId);

	
}
