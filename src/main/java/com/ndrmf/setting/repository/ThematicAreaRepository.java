package com.ndrmf.setting.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.setting.model.ThematicArea;

public interface ThematicAreaRepository extends JpaRepository<ThematicArea, UUID> {

}
