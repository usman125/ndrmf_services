package com.ndrmf.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.engine.model.FIPThematicArea;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface FIPThematicAreaRepository extends JpaRepository<FIPThematicArea, Integer>{
    @Query(value = "SELECT fipta FROM FIPThematicArea fipta JOIN fipta.fip fiptafip WHERE fiptafip.id = :userId")
    List<FIPThematicArea> getAllThematicAreasForUser(@Param("userId") UUID userId);
}
