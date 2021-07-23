package com.ndrmf.engine.repository;

import com.ndrmf.engine.model.QprToDonorSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QprToDonorSectionRepository extends JpaRepository<QprToDonorSection, UUID> {

}
