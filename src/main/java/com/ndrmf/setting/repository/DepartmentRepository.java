package com.ndrmf.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.setting.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

}
