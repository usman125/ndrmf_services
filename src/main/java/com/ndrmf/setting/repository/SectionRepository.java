package com.ndrmf.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ndrmf.setting.model.Section;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section , UUID> {
    List<Section> findAllByEnabledTrue();
    
    @Query(value = "SELECT s FROM Section s JOIN s.processType pt WHERE pt.id = :processType ORDER BY s.orderNum ASC")
    List<Section> findAllSectionsForProcessType(@Param("processType") String processType);
}
