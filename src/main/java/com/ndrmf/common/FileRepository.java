package com.ndrmf.common;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, UUID> {

}
