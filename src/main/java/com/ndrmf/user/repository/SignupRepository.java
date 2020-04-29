package com.ndrmf.user.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.user.model.Signup;

public interface SignupRepository extends JpaRepository<Signup, UUID>{
	
	@Query(value = "SELECT r FROM Signup r WHERE r.approvalStatus = :status")
	List<Signup> findRequestsForStatus(@Param("status") String requestStatus);
}
