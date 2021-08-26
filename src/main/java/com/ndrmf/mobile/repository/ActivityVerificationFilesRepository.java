package com.ndrmf.mobile.repository;

import com.ndrmf.mobile.model.ActivityVerificationFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ActivityVerificationFilesRepository extends JpaRepository<ActivityVerificationFiles, UUID> {
    @Query(value = "SELECT avf FROM ActivityVerificationFiles avf " +
            "WHERE avf.avRef.id = :avId")
    List<ActivityVerificationFiles> findRequestsByAvId(@Param("avId") UUID avId);
}
