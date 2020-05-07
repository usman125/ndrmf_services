package com.ndrmf.engine.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.Qualification;

public interface QualificationRepository extends JpaRepository<Qualification, UUID>{

}
