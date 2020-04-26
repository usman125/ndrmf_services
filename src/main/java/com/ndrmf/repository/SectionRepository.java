package com.ndrmf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndrmf.model.Section;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section , Long> {
    List<Section> findAllByActiveTrue();

    Section findBySectionKey(String sectionKey);
    List<Section> findByFormIdentityAndActiveTrue(String formIdentity);
}
