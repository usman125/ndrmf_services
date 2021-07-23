package com.ndrmf.setting.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.setting.model.CostHead;

public interface CostHeadRepository extends JpaRepository<CostHead, UUID>{
	List<CostHead> findAllByEnabled(boolean enabled);
}
