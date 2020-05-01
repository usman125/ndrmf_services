package com.ndrmf.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndrmf.setting.model.Section;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section , UUID> {
    List<Section> findAllByEnabledTrue();
}
