package com.ndrmf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.model.Accreditation;

public interface AccreditationRepository extends JpaRepository<Accreditation, Long> {
    //Accreditation findByAccUser_Username(String user);
}
