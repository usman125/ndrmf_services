package com.ndrmf.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.user.model.Organisation;

public interface OrganisationRepository extends JpaRepository<Organisation, Integer>{
	Optional<Organisation> findByName(String name);
}
