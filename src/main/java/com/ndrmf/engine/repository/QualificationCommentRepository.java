package com.ndrmf.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.QualificationComment;

public interface QualificationCommentRepository extends JpaRepository<QualificationComment, Long> {
	
}
