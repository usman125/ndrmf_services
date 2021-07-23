package com.ndrmf.setting.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.setting.model.ProcessType;

public interface ProcessTypeRepository extends JpaRepository<ProcessType, String> {
	@Query(value = "SELECT pt.name FROM ProcessType pt JOIN pt.parent ppt WHERE ppt.name = :processType")
	Set<String> getSubProcessTypes(@Param("processType") String processType);
	
	@Query(value = "SELECT pto.id FROM ProcessType pt JOIN pt.owner pto WHERE pt.name = :processType")
	UUID getPOByProcessType(@Param("processType") String processType);
	
	@Query(value = "SELECT pt FROM ProcessType pt WHERE pt.owner.id = :owner")
	List<ProcessType> getProcessTypesByOwner(@Param("owner") UUID owner);
}
