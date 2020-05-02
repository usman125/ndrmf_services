package com.ndrmf.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.setting.model.ProcessType;

public interface ProcessMetaRepository extends JpaRepository<ProcessType, Integer>{
	List<ProcessType> findByName(String name);
}
