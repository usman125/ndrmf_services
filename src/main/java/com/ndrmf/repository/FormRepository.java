package com.ndrmf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.model.Form;
import com.ndrmf.model.Section;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {

    Form findBySection(Section section);

    List<Form> findAllByActiveTrue();
}
