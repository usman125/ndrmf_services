package com.ndrmf.setting.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.setting.model.SectionTemplate;

public interface SectionTemplateRepository extends JpaRepository<SectionTemplate, UUID> {
	List<SectionTemplate> findBySectionId(UUID id);
	
	@Query(value = "SELECT st FROM SectionTemplate st JOIN st.section s WHERE st.enabled = true AND s.enabled = true AND s.processType = :processType")
	List<SectionTemplate> findTemplatesForProcessType(@Param("processType") String processType);
}
