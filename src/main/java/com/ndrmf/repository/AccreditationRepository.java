package com.ndrmf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.model.Accreditation;
import com.ndrmf.model.Section;
import com.ndrmf.user.model.User;

public interface AccreditationRepository extends JpaRepository<Accreditation, Long> {
    Accreditation findByAccreditationSection_SectionKeyAndAccUser_Username(String section, String user);




}
