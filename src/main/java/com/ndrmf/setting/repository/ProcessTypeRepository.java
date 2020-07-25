package com.ndrmf.setting.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.setting.model.ProcessType;

public interface ProcessTypeRepository extends JpaRepository<ProcessType, String> {
	@Query(value = "SELECT pt.name FROM ProcessType pt JOIN pt.parent ppt WHERE ppt.name = :processType")
	Set<String> getSubProcessTypes(@Param("processType") String processType);
}
